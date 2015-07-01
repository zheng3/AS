package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.constants.CompletionStatus;

public interface IHasCompletionStatus extends IThing{
	public static final String COMPLETION_STATUS_PROPERTY_NAME = "#completionStatus";
	
	public CompletionStatus getCompletionStatus();
}
