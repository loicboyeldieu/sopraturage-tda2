/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package sopraturage.maps.results;
/**
 * The distance component for Directions API results.
 */
public class Distance {

  /**
   * This is the numeric distance, always in meters. This is intended to be used only in
   * algorithmic situations, e.g. sorting results by some user specified metric.
   */
  public long inMeters;

  /**
   * This is the human friendly distance. This is rounded and in an appropriate unit for the
   * request. The units can be overriden with a request parameter.
   */
  public String humanReadable;

  @Override
  public String toString() {
    return humanReadable;
  }
}
