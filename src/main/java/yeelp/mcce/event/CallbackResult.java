package yeelp.mcce.event;

public class CallbackResult {

	private final CancelState cancel;
	private final ProcessState process;
	public static enum CancelState {
		CANCEL,
		PASS;
	}
	
	public static enum ProcessState {
		CANCEL,
		PASS;
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
}