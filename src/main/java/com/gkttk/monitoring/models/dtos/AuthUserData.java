package com.gkttk.monitoring.models.dtos;

import lombok.Builder;

@Builder
public record AuthUserData(String login, Boolean admin, String token) {
}
