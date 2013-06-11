package dse_domain.DTO;

import java.io.Serializable;

public class ReservationCancellationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String opSlotID;

	public ReservationCancellationDTO(String opSlotID ) {
		super();
		this.opSlotID = opSlotID;
	}

	public String getOpSlotID() {
		return opSlotID;
	}

	public void setOpSlotID(String opSlotID) {
		this.opSlotID = opSlotID;
	}

	@Override
	public String toString() {
		return "ReservationCancellationDTO [opSlotID=" + opSlotID + "]";
	}

}
