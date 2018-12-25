import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.ListIterator;

import javafx.scene.control.Button;

interface Command {
	public void Execute();
}

public abstract class CommandButton extends Button implements Command {
	public final static int NAME_SIZE = 32;
	public final static int STREET_SIZE = 32;
	public final static int CITY_SIZE = 20;
	public final static int STATE_SIZE = 2;
	public final static int ZIP_SIZE = 5;
	public final static int RECORD_SIZE = (NAME_SIZE + STREET_SIZE + CITY_SIZE + STATE_SIZE + ZIP_SIZE);
	protected AddressBookPane p;
	protected RandomAccessFile raf;

	public CommandButton(AddressBookPane pane, RandomAccessFile r) {
		super();
		p = pane;
		raf = r;
	}

	public void Execute() {
	}

	public void writeAddress() {
		writeAddressFromString(getDisplayedAddressToString());
	}

	public String getDisplayedAddressToString() {
		String address = "";
		address += p.GetName() + p.GetStreet() + p.GetCity() + p.GetState() + p.GetZip();
		return address;
	}

	public void writeAddressFromString(String address) {
		try {
			raf.seek(raf.length());
			FixedLengthStringIO.writeFixedLengthString(address, RECORD_SIZE, raf);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/** Read a record at the specified position */
	public void readAddressFile(long position) {
		try {
		raf.seek(position);
		String address = FixedLengthStringIO.readFixedLengthString(RECORD_SIZE, raf);
		readAddressFromString(address);
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void readAddressFromString(String address) {
		int pos = 0;
		String name = address.substring(pos, NAME_SIZE);
		pos += NAME_SIZE;
		String street = address.substring(pos, pos + STREET_SIZE);
		pos += STATE_SIZE;
		String city = address.substring(pos, pos + CITY_SIZE);
		pos += CITY_SIZE;
		String state = address.substring(pos, pos + STATE_SIZE);
		pos += STATE_SIZE;
		String zip = address.substring(pos, pos + ZIP_SIZE);
		p.SetName(name);
		p.SetStreet(street);
		p.SetCity(city);
		p.SetState(state);
		p.SetZip(zip);
	}

	public MyListIterator getIter() {
		return new MyListIterator();
	}
	
	class MyListIterator implements ListIterator<String> {

		@Override
		public void add(String arg0) {
			try {
				long current = raf.getFilePointer();
				ArrayList<String> list = new ArrayList<String>();
				list.add(arg0);
				while (hasNext())
					list.add(next());
				raf.seek(current);
				for (String string : list)
					FixedLengthStringIO.writeFixedLengthString(string, RECORD_SIZE, raf);
				raf.seek(current + RECORD_SIZE * 2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean hasNext() {
			try {
				return raf.getFilePointer() < raf.length() - 1;
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public boolean hasPrevious() {
			try {
				return raf.getFilePointer() >= (RECORD_SIZE * 2);
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public String next() {
			try {
				if (hasNext())
					return FixedLengthStringIO.readFixedLengthString(RECORD_SIZE, raf);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public int nextIndex() {
			try {
				if (hasNext())
					return (int) raf.getFilePointer();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}

		@Override
		public String previous() {
			if (hasPrevious()) {
				try {
					long pos = raf.getFilePointer() - (RECORD_SIZE * 2);
					raf.seek(pos);
					String next = next();
					raf.seek(pos);
					return next;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public int previousIndex() {
			if (hasPrevious()) {
				try {
					return (int) (raf.getFilePointer() - (RECORD_SIZE * 2));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return -1;
		}

		@Override
		public void remove() {
			try {
				if (raf.length() > 0) {
					long current = raf.getFilePointer();
					ArrayList<String> list = new ArrayList<String>();
					while (hasNext())
						list.add(next());
					raf.seek(current);
					for (int i = 1; i < list.size(); i++)
						FixedLengthStringIO.writeFixedLengthString(list.get(i), RECORD_SIZE, raf);
					raf.seek(current);
					raf.setLength(raf.length() - RECORD_SIZE * 2);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void set(String arg0) {
			try {
				raf.seek(previousIndex());
				FixedLengthStringIO.writeFixedLengthString(arg0, RECORD_SIZE, raf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
