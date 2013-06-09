package dao;

import dse_domain.domain.OpSlot;

public interface IReservationDAO {
	
	public OpSlot findFreeOPSlotsWithinPatientRadius(String patientID, int maxDistance);
	
}
