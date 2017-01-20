package edu.cornell.weill.boe.enterprisetools;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;

public class InfoStoreQueryHelper<E> implements Iterator<IInfoObject> {

  private int min_id;
  private String queryPredicate;
  private IInfoStore boInfoStore;
  private IInfoObjects boInfoObjects;
  private Iterator<IInfoObject> itr;

  public InfoStoreQueryHelper(IInfoStore boInfoStore, String table, String where) {
    this.min_id = 0;
    this.queryPredicate = "SELECT TOP 100 STATIC FROM " + table + " WHERE " + where;
    this.boInfoStore = boInfoStore;
  };

  @Override
  public boolean hasNext() {

    if (itr == null || !itr.hasNext()) {

      nextQuery();
    }
    return itr.hasNext();
  }
  @Override
  public IInfoObject next() {

    if (!itr.hasNext()) {
      nextQuery();
    }

    IInfoObject io;

    try {
      io = (IInfoObject) itr.next();
      this.min_id = io.getID();
      return io;
    } catch (NoSuchElementException e) {
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  private void nextQuery() {
    try {

      String queryString = queryPredicate + " AND SI_ID > " + min_id + " ORDER BY SI_ID ASC ";
      boInfoObjects = boInfoStore.query(queryString);
      this.itr = boInfoObjects.iterator();
    } catch (SDKException e) {
      System.out.println(e.getDetailMessage());
    }
  }

  @Override
  public void remove() {
  }
}
