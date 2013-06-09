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
package de.perdian.apps.downloader.core.support;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.perdian.apps.downloader.core.DownloadEngine;

public class DownloadActionList {

  static final Logger log = LogManager.getLogger(DownloadActionList.class);

  private List<DownloadAction> myActions = new CopyOnWriteArrayList<>();

  public void executeActions(final DownloadEngine engine) {
    List<DownloadAction> actions = this.getActions();
    if(actions != null && actions.size() > 0) {
      log.trace("Executing {} actions", actions.size());
      final CountDownLatch latch = new CountDownLatch(actions.size());
      for(final DownloadAction action : actions) {
        Thread actionThread = new Thread(new Runnable() {
          @Override public void run() {
            try {
              log.debug("Executing action: {}", action);
              action.execute(engine);
            } catch(Exception e) {
              log.error("Cannot execute action: " + action, e);
            } finally {
              latch.countDown();
            }
          }
        });
        actionThread.setName(DownloadAction.class.getSimpleName() + "[" + action + "]");
        actionThread.start();
      }
      try {
        latch.await();
      } catch(InterruptedException e) {
        // Ignore here
      }
    }

  }

  // ---------------------------------------------------------------------------
  // ---  Property access methods  ---------------------------------------------
  // ---------------------------------------------------------------------------

  public void addActions(Collection<? extends DownloadAction> actions) {
    this.getActions().addAll(actions);
  }
  public void addAction(DownloadAction action) {
    this.getActions().add(action);
  }
  List<DownloadAction> getActions() {
    return this.myActions;
  }
  void setActions(List<DownloadAction> actions) {
    this.myActions = actions;
  }

}