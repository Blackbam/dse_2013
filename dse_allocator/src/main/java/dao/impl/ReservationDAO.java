package dao.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import dao.IReservationDAO;


import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

public class ReservationDAO implements IReservationDAO{
	static final Logger logger = Logger.getLogger(ReservationDAO.class);
	
	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public void findStuff() {
		// finds all the reservation
		// List<Reservation> reservations = mongoTemplate.find(new
		// Query(where("doctor").is(doctor)), Reservation.class);
		//
		// List<OpSlot> opSlots = mongoTemplate.find(new
		// Query(where("reservation").in(reservations)), OpSlot.class)
		//

	}

	public OpSlot findFreeOPSlotsWithinPatientRadius(String patientID, int maxDistance) {

		Patient patient = mongoTemplate.findById(patientID, Patient.class);
		double[] patientLoc = patient.getLocation();

		Point patientLocation = new Point(patientLoc[0], patientLoc[1]);
		logger.debug("patient location: " + patientLocation.toString());

		NearQuery query = NearQuery.near(patientLocation).maxDistance(new Distance(maxDistance, Metrics.KILOMETERS));
		GeoResults<Hospital> test = mongoTemplate.geoNear(query, Hospital.class);

		logger.debug("got all near hospitals: " + test.toString());

		List<GeoResult<Hospital>> bla = test.getContent();

		for (GeoResult<Hospital> currGeo : bla) {
			logger.debug("-" + currGeo.getContent().getId() + " / " + currGeo.getContent().getName()
					+ " - distance to patient: " + currGeo.getDistance().getValue());

			// find OPSlots without reservation
			List<OpSlot> opSlots = mongoTemplate.find(
					new Query(where("reservation").exists(false).and("hospital").is(currGeo.getContent())),
					OpSlot.class);

			if (!opSlots.isEmpty()) {
				OpSlot currOpSlot = opSlots.get(0);
				logger.debug("found Op_slot without reservation: " + currOpSlot.getDateString() + " s: "
						+ currOpSlot.getStartTimeString() + " e: " + currOpSlot.getEndTimeString());

				return currOpSlot;
			}

		}

		return null;
	}

}
