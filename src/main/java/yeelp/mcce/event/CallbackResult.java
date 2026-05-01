package yeelp.mcce.event;

import org.jetbrains.annotations.NotNull;

public class CallbackResult implements Comparable<CallbackResult> {

	private final CancelState cancel;
	private final ProcessState process;
	public enum CancelState implements Comparable<CancelState> {
		CANCEL,
		PASS
    }
	
	public enum ProcessState implements Comparable<ProcessState> {
		CANCEL,
		PASS
    }
	
	public CallbackResult(ProcessState process, CancelState cancel) {
		this.cancel = cancel;
		this.process = process;
	}
	
	public CallbackResult(ProcessState process) {
		this(process, CancelState.PASS);
	}
	
	public CallbackResult(CancelState cancel) {
		this(ProcessState.PASS, cancel);
	}
	
	public CallbackResult() {
		this(ProcessState.PASS, CancelState.PASS);
	}
	
	public CancelState getCancelState() {
		return this.cancel;
	}
	
	public ProcessState getProcessState() {
		return this.process;
	}

	@Override
	public int compareTo(@NotNull CallbackResult o) {
		int processCompare = this.getProcessState().compareTo(o.getProcessState());
		return processCompare == 0 ? this.getCancelState().compareTo(o.getCancelState()) : processCompare;
	}

	public CallbackResult mergeResults(CallbackResult other) {
		return new CallbackResult(this.getProcessState() == ProcessState.PASS ? other.getProcessState() : this.getProcessState(), this.getCancelState() == CancelState.PASS ? other.getCancelState() : this.getCancelState());
	}
}