/*
 * Copyright (C) 2011 lightcouch.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lightcouch;

import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Query parameters to append to find requests.
 * <p>Example: 
 * <pre>
 * dbClient.find(Foo.class, "doc-id", new Params().revsInfo().attachments());
 * </pre>
 * @see CouchDbClientBase#find(Class, String, Params)
 * @since 0.0.6
 * @author Ahmed Yehia
 *
 */
@Deprecated
public class Params {
    private final Map<String, String> paramMap = new LinkedHashMap<>();

    public Params revsInfo() {
        return addTrueFlag("revs_info");
    }

    public Params attachments() {
        return addTrueFlag("attachments");
    }

    public Params revisions() {
        return addTrueFlag("revs");
    }

    public Params rev(String rev) {
        return addParam(CouchConstants.PARAM_REVISION, rev);
    }

    public Params conflicts() {
        return addTrueFlag("conflicts");
    }

    public Params localSeq() {
        return addTrueFlag("local_seq");
    }

    private Params addTrueFlag(String paramName) {
        return addParam(paramName, "true");
    }

    public Params addParam(String name, String value) {
        paramMap.put(name, value);
        return this;
    }

    @Nullable
    public List<String> getParams() {
        if (paramMap.isEmpty()) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (var entry : paramMap.entrySet()) {
            String encodedName = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            list.add(encodedName + '=' + encodedValue);
        }
        return list;
    }

    Map<String, String> getParamMap() {
        return Collections.unmodifiableMap(paramMap);
    }
}
