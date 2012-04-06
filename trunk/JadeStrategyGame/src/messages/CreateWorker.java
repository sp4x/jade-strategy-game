package messages;

import jade.core.AID;
import jade.util.leap.Serializable;

public class CreateWorker implements Serializable {
	AID aid;
	int x;
	int y;
	
	public CreateWorker(AID aid, int x, int y) {
		super();
		this.aid = aid;
		this.x = x;
		this.y = y;
	}
	
	public AID getAid() {
		return aid;
	}

	public void setAid(AID aid) {
		this.aid = aid;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
