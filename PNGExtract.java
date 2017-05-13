import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class PNGExtract {
	public static final byte[] PNG_HEADER = {
		(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a
	};

	public static final byte[] PNG_EOF = {
		0x49, 0x45, 0x4e, 0x44, (byte) 0xae, 0x42, 0x60, (byte) 0x82
	};

	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.out.println("Usage: PNGExtract in.bin out-dir");
			System.exit(1);
		}
		try(InputStream in = new FileInputStream(args[0])) {
			extract(in, args[1]);
		}
	}

	public static boolean match(InputStream in, byte[] signature)
			throws IOException {
		int b = -1;
		int i = 0;
		while((b = in.read()) != -1) {
			if((byte) b == signature[i]) {
				i++;
			} else {
				i = 0;
			}
			if(i == signature.length) {
				return true;
			}
		}
		return false;
	}

	public static void copy(InputStream in, OutputStream out, byte[] end)
			throws IOException {
		int b = -1;
		int i = 0;
		while((b = in.read()) != -1) {
			out.write(b);
			if((byte) b == end[i]) {
				i++;
			} else {
				i = 0;
			}
			if(i == end.length) {
				return;
			}
		}
	}

	public static void extract(InputStream in, String path)
			throws IOException {
		int i = 0;
		int b = -1;
		while(match(in, PNG_HEADER)) {
			try(OutputStream out = new FileOutputStream(
						String.format("%s/img-%04d.png",
							path, i++))) {
				out.write(PNG_HEADER);
				copy(in, out, PNG_EOF);
			}
		}
	}
}
