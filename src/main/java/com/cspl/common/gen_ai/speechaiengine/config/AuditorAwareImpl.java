package com.cspl.common.gen_ai.speechaiengine.config;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author vineet.rajput
 * @use: The type Auditor aware impl.
 * The type Auditor aware impl.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Gets current auditor.
     *
     * @return the current auditor
     */
    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        // Return the current user or system
        return Optional.of(AppConstants.SYSTEM_VALUE);
    }
}