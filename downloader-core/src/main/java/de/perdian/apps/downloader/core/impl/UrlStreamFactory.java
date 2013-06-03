/*
 * Copyright 2013 Christian Robert
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
package de.perdian.apps.downloader.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import de.perdian.apps.downloader.core.DownloadStreamFactory;

/**
 * {@link DownloadStreamFactory} implementation based on an underlying
 * {@code java.net.URL} object
 *
 * @author Christian Robert
 */

public class UrlStreamFactory implements DownloadStreamFactory {

  static final long serialVersionUID = 1L;

  private URL myUrl = null;

  public UrlStreamFactory(URL url) {
    this.setUrl(Objects.requireNonNull(url, "Parameter 'url' must not be null"));
  }

  @Override
  public InputStream openStream() throws IOException {
    return this.getUrl().openStream();
  }

  @Override
  public long size() throws IOException {
    URLConnection urlConnection = this.getUrl().openConnection();
    return urlConnection.getContentLengthLong();
  }

  // ---------------------------------------------------------------------------
  // --- Property access methods -----------------------------------------------
  // ---------------------------------------------------------------------------

  URL getUrl() {
    return this.myUrl;
  }
  private void setUrl(URL url) {
    this.myUrl = url;
  }

}