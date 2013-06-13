package dao.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import dao.IReservationDAO;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.OpSlot.Type;
import dse_domain.domain.Patient;

public class ReservationMongoDAO implements IReservationDAO {
	static final Logger logger = Logger.getLogger(ReservationMongoDAO.class);

	private MongoOperations mongo;

	public ReservationMongoDAO(MongoOperations mongo) {
		this.mongo = mongo;
	}

	/**
	 * mongo geo search for near hospitals
	 * 
	 * @param maxDistance
	 * @param patient
	 * @return
	 */
	private GeoResults<Hospital> findNearHospitals(int maxDistance, Patient patient) {
		double[] patientLoc = patient.getLocation();
		Point patientLocation = new Point(patientLoc[0], patientLoc[1]);
		logger.debug("patient location: " + patientLocation.toString());

		NearQuery geoQuery = NearQuery.near(patientLocation).maxDistance(new Distance(maxDistance, Metrics.KILOMETERS));
		GeoResults<Hospital> test = mongo.geoNear(geoQuery, Hospital.class);

		logger.debug("got near enough hospitals: " + test.toString());

		return test;
	}

	@Override
	public OpSlot findFreeOPSlotInNearHospital(int maxDistance, Patient patient, Date startDate, Date endDate,
			int minTime, Type type) {
		if (patient == null) {
			logger.info("findFreeOPSlotInNearHospital - passed patient is null. finding op_slot aborted");
			return null;
		}

		GeoResults<Hospital> test = findNearHospitals(maxDistance, patient);
		List<GeoResult<Hospital>> geoResults = test.getContent();

		OpSlot foundOpSlot = null;

		for (GeoResult<Hospital> currGeo : geoResults) {
			logger.debug("- proceeding with " + currGeo.getContent().getName() + " - distance to patient: "
					+ currGeo.getDistance().getValue());

			List<OpSlot> opSlots = findFreeOPSlotsInHospitalSortedList(startDate, endDate, minTime,
					currGeo.getContent(), type);

			if (!opSlots.isEmpty()) {
				// get first op_slot in the sorted list
				foundOpSlot = opSlots.get(0);
				return foundOpSlot;
			}

		}
		logger.debug("no free op_slot found");
		return null;
	}

	@Override
	public List<OpSlot> findFreeOPSlotsInHospitalSortedList(Date startDate, Date endDate, int minTime,
			Hospital hospital, Type type) {

		// op_slot date greater than or equal to startDate, and date less than or equal
		// to endDate
		Criteria dateQuery = where("date").gte(startDate).lte(endDate);

		// op_slot length greater than or equal to minTime
		Criteria lengthQuery = where("length").gte(minTime);

		// find OPSlots in the current hospital where no reservation exists yet
		Criteria reservationQuery = where("reservation").exists(false).and("hospital").is(hospital);

		// find correct type
		Criteria typeQuery = where("type").is(type.toString());

		// reservationQuery + dateQuery + lengthQuery
		Query query = new Query(reservationQuery.andOperator(dateQuery.andOperator(lengthQuery.andOperator(typeQuery))));

		List<OpSlot> opSlots = mongo.find(query, OpSlot.class);

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

	@Override
	public Patient findPatient(String patientID) {
		return mongo.findById(patientID, Patient.class);
	}

	@Override
	public Doctor findDoctor(String doctorID) {
		return mongo.findById(doctorID, Doctor.class);
	}

	@Override
	public void updateOpSlot(OpSlot opSlot) {
		mongo.save(opSlot);
		logger.info("Updated op_slot in database");
	}

	private void debugLogs() {
		List<OpSlot> slots = mongo.findAll(OpSlot.class);
		logger.debug("DEBUG after update");
		for (OpSlot curr : slots) {
			if (curr.getReservation() != null)
				logger.debug("  " + curr.getId() + "/" + curr.getDateString() + "/" + curr.getStartTimeString() + "-"
						+ curr.getEndTimeString() + "/" + curr.getTypeString() + "/ Reservation: "
						+ curr.getReservation().getDoctor() + "/" + curr.getReservation().getPatient());

			else
				logger.debug("  " + curr.getId() + "/" + curr.getDateString() + "/" + curr.getStartTimeString() + "-"
						+ curr.getEndTimeString() + "/" + curr.getTypeString() + "/ Reservation: NOPE");
		}
	}

	@Override
	public void save(Doctor doctor) {
		mongo.save(doctor);
	}

	@Override
	public void save(Patient patient) {
		mongo.save(patient);
	}
}
