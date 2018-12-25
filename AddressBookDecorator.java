import java.io.RandomAccessFile;
import java.util.ListIterator;
import java.util.Stack;

public class AddressBookDecorator {
	private CareTaker careTaker;
	private int currentMomento;

	private AddButton jbtAdd;
	private UndoButton jbtUndo;
	private RedoButton jbtRedo;

	public AddressBookDecorator(AddressBookPane pane) {
		currentMomento = 0;
		careTaker = new CareTaker();
		
		jbtAdd = new AddButton(pane, pane.getRaf(), careTaker);
		jbtUndo = new UndoButton(pane, pane.getRaf(), careTaker);
		jbtRedo = new RedoButton(pane, pane.getRaf(), careTaker);
		
		pane.getButtonsPane().getChildren().addAll(jbtAdd,jbtUndo,jbtRedo);
		
		jbtAdd.setOnAction(pane.ae);
		jbtUndo.setOnAction(pane.ae);
		jbtRedo.setOnAction(pane.ae);
		
		jbtUndo.setDisable(true);
		jbtRedo.setDisable(true);
	}

	class Momento {
		private String mometos;

		public Momento(String momentos) {
			this.mometos = momentos;
		}

		public String getMomentoValue() {
			return this.mometos;
		}
	}

	class CareTaker {
		Stack<Momento> addresses = new Stack<Momento>();

		public void addMomento(Momento momento) {
			this.addresses.push(momento);
		}

		public Momento getMomento() {
			return this.addresses.pop();
		}

		public void clear() {
			this.addresses.clear();
		}

		public int size() {
			return this.addresses.size();
		}
	}

	class CommandButtonMomento extends CommandButton {
		protected CareTaker careTaker;

		public CommandButtonMomento(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
			super(pane, r);
			if (careTaker == null)
				throw new IllegalArgumentException("Invalid CareTaker.");
			this.careTaker = careTaker;
		}

		public void display(ListIterator<?> iter) {
			while(iter.hasNext())
				iter.next();
			if(iter.hasPrevious())
				readAddressFile(iter.previousIndex());
			else 
				p.clearTextFields();
		}
	}

	class AddButton extends CommandButtonMomento {
		public AddButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
			super(pane, r, careTaker);
			this.setText("Add");
		}

		@Override
		public void Execute() {
			ListIterator<String> iter = getIter();
			while (iter.hasNext())
				iter.next();
			iter.add(getDisplayedAddressToString());
			careTaker.clear();
			currentMomento++;
			jbtRedo.setDisable(true);
			jbtUndo.setDisable(false);
		}
	}

	class UndoButton extends CommandButtonMomento {
		public UndoButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
			super(pane, r, careTaker);
			this.setText("Undo");
		}

		@Override
		public void Execute() {
			if (currentMomento > 0) {
				ListIterator<String> iter = getIter();
				while (iter.hasNext())
					iter.next();
				String momento = iter.previous();
				iter.remove();
				display(iter);
				careTaker.addMomento(new Momento(momento));
				currentMomento--;
				jbtRedo.setDisable(false);
				if(currentMomento == 0)
					this.setDisable(true);
			}
		}
	}

	class RedoButton extends CommandButtonMomento {
		public RedoButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
			super(pane, r, careTaker);
			this.setText("Redo");
		}

		@Override
		public void Execute() {
			if (careTaker.size() > 0) {
				ListIterator<String> iter = getIter();
				while (iter.hasNext())
					iter.next();
				iter.add(careTaker.getMomento().getMomentoValue());
				display(iter);
				currentMomento++;
				jbtUndo.setDisable(false);
				if(careTaker.size() == 0)
					jbtRedo.setDisable(true);
			}
		}
	}
}