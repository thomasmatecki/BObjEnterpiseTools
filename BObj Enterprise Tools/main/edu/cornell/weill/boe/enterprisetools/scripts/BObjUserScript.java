package edu.cornell.weill.boe.enterprisetools.scripts;

import com.crystaldecisions.sdk.exception.SDKException;
import edu.cornell.weill.boe.enterprisetools.InfoStoreQueryHelper;

public interface BObjUserScript {
	public void run(InfoStoreQueryHelper<?> isqh) throws SDKException;
}
