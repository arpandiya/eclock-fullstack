package com.eclock.backend.auth.service;

import com.eclock.backend.auth.records.RegisterRequest;


public interface IUserService {
    boolean register(RegisterRequest registerRequest);

}
