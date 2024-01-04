package com.gkttk.monitoring.models.dtos;

import lombok.Builder;

@Builder
public record JwtRequest(String login, String password) {
}
