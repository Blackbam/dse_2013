package dse_domain.DTO;

import java.io.Serializable;

public class ReservationSuccessNotificationDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private ReservationDTO reservationRequest;
	private OpSlotDTO assignedOpSlot;

	public ReservationSuccessNotificationDTO(ReservationDTO reservationRequest, OpSlotDTO assignedOpSlot) {
		super();
		this.reservationRequest = reservationRequest;
		this.assignedOpSlot = assignedOpSlot;
	}

	public ReservationDTO getReservationRequest() {
		return reservationRequest;
	}

	public void setReservationRequest(ReservationDTO reservationRequest) {
		this.reservationRequest = reservationRequest;
	}

	public OpSlotDTO getAssignedOpSlot() {
		return assignedOpSlot;
	}

	public void setAssignedOpSlot(OpSlotDTO assignedOpSlot) {
		this.assignedOpSlot = assignedOpSlot;
	}

	@Override
	public String toString() {
		return "ReservationSuccessNotificationDTO [reservationRequest=" + reservationRequest + ", assignedOpSlot="
				+ assignedOpSlot + "]";
	}

}
