package server_side;


public interface Server {
	public void start(ClientHandler c) throws Exception;
	public void stop();
	
}
