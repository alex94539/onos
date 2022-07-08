/*
 * Copyright 2022-present Open Networking Foundation
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

package org.onosproject.netconf.callhome;

import java.security.PublicKey;
import java.util.Map;

import org.onosproject.net.DeviceId;
import org.onosproject.netconf.NetconfDeviceInfo;
import org.onosproject.netconf.NetconfException;
import org.onosproject.netconf.NetconfSession;

public interface NetconfCallHomeController {
    NetconfSession createNetconfSession(NetconfDeviceInfo netconfDeviceInfo) throws NetconfException;

    NetconfDeviceInfo createDeviceInfo(DeviceId deviceId, boolean isMaster) throws NetconfException;

    void removeSession(DeviceId deviceId);

    Map<DeviceId, CallHomeSSHSession> getSessionMap();

    boolean isCallHomeDeviceId(DeviceId deviceId);

    PublicKey decodePublicKeyString(String key);

    String encodePublicKey(PublicKey key);

    void addListener(NetconfCallHomeSSHListener listener);

    void removeListener(NetconfCallHomeSSHListener listener);

    NetconfCallHomeDeviceConfig registerDevice(CallHomeConfigBuilder builder);

    void unregisterDevice(DeviceId deviceId);
}
