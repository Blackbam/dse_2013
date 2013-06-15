package dse_domain.DTO;

import java.io.Serializable;

/**
 * Notification that a reservation failed.
 */
public class ReservationFailNotificationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private ReservationDTO reservationRequest;

	private String failureReason;

	public ReservationFailNotificationDTO(ReservationDTO request, String failureReason) {
		super();
		this.reservationRequest = request;
		this.failureReason = failureReason;
	}

	public ReservationDTO getReservationRequest() {
		return reservationRequest;
	}

	public void setReservationRequest(ReservationDTO reservation) {
		this.reservationRequest = reservation;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String reason) {
		this.failureReason = reason;
	}

	@Override
	public String toString() {
		return "ReservationFailNotificationDTO [request=" + reservationRequest + ", failureReason=" + failureReason
				+ "]";
	}

}
