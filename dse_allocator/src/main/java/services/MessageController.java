package services;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
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
import org.springframework.stereotype.Controller;

import dao.IReservationDAO;
import dao.impl.ReservationDAO;
import dse_domain.DTO.ReservationDTO;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

@Controller
public class MessageController {
	static final Logger logger = Logger.getLogger(MessageController.class);

	@Autowired
	AmqpTemplate amqpTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	IReservationDAO reservationDAO;

	public MessageController() {
		this.reservationDAO = new ReservationDAO();
	}

	public void handleReservationRequest(Object message) {
		logger.info("received reservation request " + message);

		try {
			if (message instanceof ReservationDTO) {

				logger.info("message is of instance ReservationDTO! " + message);

				ReservationDTO receivedMsg = (ReservationDTO) message;

				String patientID = receivedMsg.getPatientID();
				int maxDistance = receivedMsg.getMaxDistance();

				Date startDate = receivedMsg.getDateStart();
				Date endDate = receivedMsg.getDateEnd();

				int minTime = receivedMsg.getMinTime();

				// DAO not yet working (injection of mongoDB fails)
				// OpSlot foundOpSlot = reservationDAO.findFreeOPSlotsWithinPatientRadius(patientID,
				// maxDistance);

				Patient patient = mongoTemplate.findById(patientID, Patient.class);
				double[] patientLoc = patient.getLocation();

				Point patientLocation = new Point(patientLoc[0], patientLoc[1]);
				logger.debug("patient location: " + patientLocation.toString());

				NearQuery geoQuery = NearQuery.near(patientLocation).maxDistance(
						new Distance(maxDistance, Metrics.KILOMETERS));
				GeoResults<Hospital> test = mongoTemplate.geoNear(geoQuery, Hospital.class);

				logger.debug("got all near hospitals: " + test.toString());

				List<GeoResult<Hospital>> bla = test.getContent();
				OpSlot foundOpSlot = null;

				for (GeoResult<Hospital> currGeo : bla) {
					logger.debug("-" + currGeo.getContent().getId() + " / " + currGeo.getContent().getName()
							+ " - distance to patient: " + currGeo.getDistance().getValue());

					// op_slot date greater than or equal to startDate and date less than or equal
					// to endDate
					Criteria dateQuery = where("date").gte(startDate).lte(endDate);

					// op_slot length greater than or equal to minTime
					Criteria lengthQuery = where("length").gte(minTime);

					// find OPSlots in the current hospital where no reservation exists yet
					Criteria reservationQuery = where("reservation").exists(false).and("hospital")
							.is(currGeo.getContent());

					// reservationQuery + dateQuery + lengthQuery
					Query query = new Query(reservationQuery.andOperator(dateQuery.andOperator(lengthQuery)));

					List<OpSlot> opSlots = mongoTemplate.find(query, OpSlot.class);

					logger.debug("Queried " + opSlots.size() + " op_slots");
					
					
					// sort the result list
					Collections.sort(opSlots, new Comparator<OpSlot>() {

						@Override
						public int compare(OpSlot o1, OpSlot o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});

					for (OpSlot curr : opSlots) {
						logger.debug("-- queried " + curr.getDateString() + " s: " + curr.getStartTimeString() + " e: "
								+ curr.getEndTimeString() + " ");
					}

					if (!opSlots.isEmpty()) {
						foundOpSlot = opSlots.get(0);
						logger.debug("found Op_slot without reservation: " + foundOpSlot.getDateString() + " time: "
								+ foundOpSlot.getStartTimeString() + " - " + foundOpSlot.getEndTimeString()
								+ " in hospital: " + foundOpSlot.getHospital().getName());
						break;
					}

				}

				if (foundOpSlot != null) {
					logger.info("Reservation finished processing.. found OpSlot: " + foundOpSlot.getDateString()
							+ " / " + foundOpSlot.getStartTimeString() + "-" + foundOpSlot.getEndTimeString()
							+ " in hospital: " + foundOpSlot.getHospital().getName());
				}

				// List<Patient> patient = mongoTemplate.find(new
				// Query(where("id").is(patientID)),Patient.class);

				// List<Hospital> debugHosp = mongoTemplate.findAll(Hospital.class);
				// logger.debug("display ALL hospitals (for debug):");
				// for (Hospital curr : debugHosp) {
				// logger.debug(curr.getId() + " / " + curr.getName() + " - location: " +
				// curr.getLocation()[0] + "/"
				// + curr.getLocation()[1]);
				// }

				// Criteria crit =
				// where("location").nearSphere(patientLocation).maxDistance(maxDistance);
				//
				// List<Hospital> fuubar = mongoTemplate.find(new Query(crit), Hospital.class);
				//
				// logger.debug("found nearSphere hospitals: " + fuubar.size());
				// for (Hospital curr : fuubar) {
				// logger.debug("near_ "+curr.getId() + " / " + curr.getName() + " - location: " +
				// curr.getLocation()[0] + "/"
				// + curr.getLocation()[1]);
				// }

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
