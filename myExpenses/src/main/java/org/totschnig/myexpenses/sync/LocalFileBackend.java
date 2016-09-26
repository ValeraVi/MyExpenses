package org.totschnig.myexpenses.sync;

import android.content.Context;
import android.support.v4.provider.DocumentFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.totschnig.myexpenses.sync.json.AdapterFactory;
import org.totschnig.myexpenses.sync.json.TransactionChange;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

class LocalFileBackend implements SyncBackend {

  private final Gson gson;

  LocalFileBackend(DocumentFile baseDir, Context context) {
    this.baseDir = baseDir;
    this.context = context;
    gson = new GsonBuilder()
        .registerTypeAdapterFactory(AdapterFactory.create())
        .create();
  }

  DocumentFile baseDir;
  Context context;

  private long getLastSequence() {
    for (DocumentFile file: baseDir.listFiles()) {
      file.getName()
    }
    return 0;
  }


  @Override
  public boolean lock() {
    return false;
  }

  @Override
  public List<TransactionChange> getChangeSetSince(long sequenceNumber) {
    return null;
  }

  @Override
  public long writeChangeSet(List<TransactionChange> changeSet) throws IOException {
    long nextSequence = getLastSequence() + 1;
    DocumentFile changeSetFile = baseDir.createFile("text/json", "_" + nextSequence + ".json");
    OutputStreamWriter out = new OutputStreamWriter(
        context.getContentResolver().openOutputStream(changeSetFile.getUri()), "UTF-8");
    for (TransactionChange change: changeSet) {
      out.write(gson.toJson(change));
    }
    out.close();
    return  nextSequence;
  }

  @Override
  public boolean unlock() {
    return false;
  }
}
