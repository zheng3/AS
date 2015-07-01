package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.constants.CompletionStatus;

public interface IHasMutableCompletionStatus
	extends IHasCompletionStatus{

	public void setCompletionStatus(CompletionStatus completionStatus);
}
