/*
 * Copyright (c) 2020 François Onimus
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

package com.baiyi.opscloud.sshserver;

import lombok.Getter;

/**
 * Map enum color to jline ones
 */

public enum PromptColor {

    BLACK(0),
    RED(1),
    GREEN(2),
    YELLOW(3),
    BLUE(4),
    MAGENTA(5),
    CYAN(6),
    WHITE(7),
    BRIGHT(8),
    COLOR64(64),
    COLOR128(128),
    COLOR129(129),
    COLOR130(130),
    COLOR131(131),
    COLOR132(132),
    COLOR250(250)
    ;

    @Getter
    private final int value;

    PromptColor(int value) {
        this.value = value;
    }

    public int toJlineAttributedStyle() {
        return value;
    }
}
