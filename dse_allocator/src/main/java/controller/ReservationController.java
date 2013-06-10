package controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;

import com.mongodb.WriteResult;

import dao.IReservationDAO;
import dao.impl.ReservationMongoDAO;
import dse_domain.DTO.ReservationDTO;
import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.domain.Reservation;

@Controller
public class ReservationController {
	static final Logger logger = Logger.getLogger(ReservationController.class);

	@Autowired
	AmqpTemplate amqpTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	//@Autowired
	IReservationDAO reservationDAO;

	public ReservationController() {
		//this.reservationDAO = new ReservationDAO();
	}

	public void handleReservationRequest(Object message) {
		logger.info("Received Message: " + message);
		

		try {
			//reservationDAO.findStuff();
			
			
			if (message instanceof ReservationDTO) {
				

				logger.info("Message is instance of ReservationDTO, processing..");

				ReservationDTO receivedDTO = (ReservationDTO) message;

				String patientID = receivedDTO.getPatientID();
				String doctorID = receivedDTO.getDoctorID();
				int maxDistance = receivedDTO.getMaxDistance();
				Date startDate = receivedDTO.getDateStart();
				Date endDate = receivedDTO.getDateEnd();
				int minTime = receivedDTO.getMinTime();

				// DAO not yet working (injection of mongoDB fails)
				// OpSlot foundOpSlot = reservationDAO.findFreeOPSlotsWithinPatientRadius(patientID,
				// maxDistance);

				Patient patient = mongoTemplate.findById(patientID, Patient.class);
				
				GeoResults<Hospital> test = findNearHospitals(maxDistance, patient);

				List<GeoResult<Hospital>> bla = test.getContent();
				OpSlot foundOpSlot = null;
				
				for (GeoResult<Hospital> currGeo : bla) {
					logger.debug("- proceeding with " + currGeo.getContent().getName() + " - distance to patient: "
							+ currGeo.getDistance().getValue());

					List<OpSlot> opSlots = findFreeOPSlotsSortedList(startDate, endDate, minTime, currGeo.getContent());

					if (!opSlots.isEmpty()) {
						// get first op_slot in the sorted list
						foundOpSlot = opSlots.get(0);
						break;
					}

				}

				if (foundOpSlot != null) {
					logger.info("Reservation finished processing.. found OpSlot: " + foundOpSlot.getDateString()
							+ " / " + foundOpSlot.getStartTimeString() + "-" + foundOpSlot.getEndTimeString()
							+ " in hospital: " + foundOpSlot.getHospital().getName());

					Doctor doctor = mongoTemplate.findById(doctorID, Doctor.class);

					Reservation reservation = new Reservation(doctor, patient);
					foundOpSlot.setReservation(reservation);
					//new ObjectId(entity.getId())
					//WriteResult fuck = mongoTemplate.updateFirst(new Query(where("_id").is(new ObjectId(foundOpSlot.getId()))), new Update().set("reservation", foundOpSlot.getReservation()), OpSlot.class);
					//WriteResult fuck = mongoTemplate.updateFirst(new Query(where("id").is(foundOpSlot.getId())), new Update().set("reservation", foundOpSlot.getReservation()), OpSlot.class);
					
					//logger.error("getError - " + fuck.getError());			
					//logger.debug("fuck: " + fuck.getField("hospital") + "/" + fuck.getField("date") + "/" + fuck.getField("reservation") + " number of affected documents: " + fuck.getN());
					
					mongoTemplate.save(foundOpSlot);					
					logger.info("Updated op_slot in database");
					
					
					List<OpSlot> slots = mongoTemplate.findAll(OpSlot.class);
					logger.debug("DEBUG after update");
					for(OpSlot curr: slots){
						if(curr.getReservation()!= null)
						logger.debug("  " + curr.getId() + "/" +curr.getDateString() +"/" +curr.getStartTimeString() +"-"
								+curr.getEndTimeString() +"/" +curr.getTypeString() + "/ Reservation: " + curr.getReservation().getDoctor()
								+ "/" +curr.getReservation().getPatient());
						
						else
							logger.debug("  " + curr.getId() + "/" +curr.getDateString() +"/" +curr.getStartTimeString() +"-"
									+curr.getEndTimeString() +"/" +curr.getTypeString() + "/ Reservation: NOPE");
					}

				} else {
					logger.info("Reservation not possible.. no free OpSlots found");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	private GeoResults<Hospital> findNearHospitals(int maxDistance, Patient patient) {
		double[] patientLoc = patient.getLocation();
		Point patientLocation = new Point(patientLoc[0], patientLoc[1]);
		logger.debug("patient location: " + patientLocation.toString());

		NearQuery geoQuery = NearQuery.near(patientLocation).maxDistance(
				new Distance(maxDistance, Metrics.KILOMETERS));
		GeoResults<Hospital> test = mongoTemplate.geoNear(geoQuery, Hospital.class);
		logger.debug("got near enough hospitals: " + test.toString());
		return test;
	}
	

	private List<OpSlot> findFreeOPSlotsSortedList(Date startDate, Date endDate, int minTime, Hospital hospital) {

		// op_slot date greater than or equal to startDate and date less than or equal
		// to endDate
		Criteria dateQuery = where("date").gte(startDate).lte(endDate);

		// op_slot length greater than or equal to minTime
		Criteria lengthQuery = where("length").gte(minTime);

		// find OPSlots in the current hospital where no reservation exists yet
		Criteria reservationQuery = where("reservation").exists(false).and("hospital").is(hospital);

		// reservationQuery + dateQuery + lengthQuery
		Query query = new Query(reservationQuery.andOperator(dateQuery.andOperator(lengthQuery)));

		List<OpSlot> opSlots = mongoTemplate.find(query, OpSlot.class);

		logger.debug("Queried " + opSlots.size() + " op_slot(s)");

		// sort the result list
		Collections.sort(opSlots, new Comparator<OpSlot>() {

			@Override
			public int compare(OpSlot o1, OpSlot o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});

		for (OpSlot curr : opSlots) {
			logger.debug("-- queried " + curr.getDateString() + " / " + curr.getStartTimeString() + " - "
					+ curr.getEndTimeString() + " ");
		}

		return opSlots;
	}

}
