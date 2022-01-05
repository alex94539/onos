/*
 * Copyright 2021-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.net.behaviour.upf;

import com.google.common.annotations.Beta;
import org.onlab.packet.Ip4Address;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A structure representing the UE Session on the UPF-programmable device.
 * Provides means to set up the UE Session in the downlink direction.
 */
@Beta
public final class SessionDownlink implements UpfEntity {
    // Match Keys
    private final Ip4Address ueAddress;
    // Action parameters
    private final Byte tunPeerId;
    private final boolean buffering;
    private final boolean dropping;

    private SessionDownlink(Ip4Address ipv4Address,
                            Byte tunPeerId,
                            boolean buffering,
                            boolean drop) {
        this.ueAddress = ipv4Address;
        this.tunPeerId = tunPeerId;
        this.buffering = buffering;
        this.dropping = drop;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }

        SessionDownlink that = (SessionDownlink) object;

        return this.buffering == that.buffering &&
                this.dropping == that.dropping &&
                Objects.equals(ueAddress, that.ueAddress) &&
                Objects.equals(tunPeerId, that.tunPeerId);
    }

    public int hashCode() {
        return java.util.Objects.hash(ueAddress, tunPeerId, buffering, dropping);
    }

    @Override
    public String toString() {
        return "UESessionDL{" + matchString() + " -> " + actionString() + "}";
    }

    private String matchString() {
        return "Match(ue_addr=" + this.ueAddress() + ")";
    }

    private String actionString() {
        StringBuilder actionStrBuilder = new StringBuilder("(");
        if (this.needsBuffering() && this.needsDropping()) {
            actionStrBuilder.append("BUFF+DROP, ");
        } else if (this.needsBuffering()) {
            actionStrBuilder.append("BUFF, ");
        } else if (this.needsDropping()) {
            actionStrBuilder.append("DROP, ");
        } else {
            actionStrBuilder.append("FWD, ");
        }
       return actionStrBuilder.append(" tun_peer=").append(this.tunPeerId()).append(")")
               .toString();
    }

    /**
     * True if this UE Session needs buffering of the downlink traffic.
     *
     * @return true if the UE Session needs buffering.
     */
    public boolean needsBuffering() {
        return buffering;
    }

    /**
     * True if this UE Session needs dropping of the downlink traffic.
     *
     * @return true if the UE Session needs dropping.
     */
    public boolean needsDropping() {
        return dropping;
    }

    /**
     * Get the UE IP address of this downlink UE session.
     *
     * @return UE IP address
     */
    public Ip4Address ueAddress() {
        return ueAddress;
    }

    /**
     * Get the GTP tunnel peer ID that is set by this UE Session rule.
     *
     * @return GTP tunnel peer ID
     */
    public Byte tunPeerId() {
        return tunPeerId;
    }

    @Override
    public UpfEntityType type() {
        return UpfEntityType.SESSION_DOWNLINK;
    }

    public static class Builder {
        private Ip4Address ueAddress = null;
        private Byte tunPeerId = null;
        private boolean buffer = false;
        private boolean drop = false;

        public Builder() {

        }

        /**
         * Set the UE IP address that this downlink UE session rule matches on.
         *
         * @param ueAddress UE IP address
         * @return This builder object
         */
        public Builder withUeAddress(Ip4Address ueAddress) {
            this.ueAddress = ueAddress;
            return this;
        }

        /**
         * Set the GTP tunnel peer ID that is set by this UE Session rule.
         *
         * @param tunnelPeerId GTP tunnel peer ID
         * @return This builder object
         */
        public Builder withGtpTunnelPeerId(Byte tunnelPeerId) {
            this.tunPeerId = tunnelPeerId;
            return this;
        }

        /**
         * Sets whether to buffer downlink UE session traffic or not.
         *
         * @param buffer True if request to buffer, false otherwise
         * @return This builder object
         */
        public Builder needsBuffering(boolean buffer) {
            this.buffer = buffer;
            return this;
        }

        /**
         * Sets whether to drop downlink UE session traffic or not.
         *
         * @param drop True if request to buffer, false otherwise
         * @return This builder object
         */
        public Builder needsDropping(boolean drop) {
            this.drop = drop;
            return this;
        }

        public SessionDownlink build() {
            // Match fields are required
            checkNotNull(ueAddress, "UE address must be provided");
            return new SessionDownlink(ueAddress, tunPeerId, buffer, drop);
        }
    }
}
