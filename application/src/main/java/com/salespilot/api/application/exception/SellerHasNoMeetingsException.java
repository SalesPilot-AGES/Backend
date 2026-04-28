package com.salespilot.api.application.exception;

import java.util.UUID;

public class SellerHasNoMeetingsException extends RuntimeException {
    public SellerHasNoMeetingsException(UUID sellerId) {
        super("Seller has no meetings. ID: " + sellerId);
    }
}
