import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

class comp implements Comparator<Node> { 
    public int compare(Node x, Node y) 
    { 
        return x.prob - y.prob;
    } 
} 


public class Huffman {
	private String Path = "";
	
	public void set_path(String p) {
		Path = p;
	}
	public String get_path() {
		return Path;
	}
	
	public String read () { // Reads a file 
		File file = new File (Path);
		String Data = "";
		try {
		Scanner read = new Scanner(file);
		read.useDelimiter("\\Z"); // keep reading until eof
		Data = read.next();
		read.close();
		}
		
		catch (IOException  exp) {
			exp.printStackTrace();
		}
		return Data;
	}
	
	public void GetCode(Node root, String start, ArrayList<String> code, ArrayList<Character> c) {
		
		if (root.left == null && root.right == null && Character.isLetter(root.c)) {
			//System.out.print(root.c + " : ");
			//System.out.println(start);
			
			code.add(start);
			c.add(root.c); 
			return;
		}
		
		GetCode(root.left, start + "0", code, c);
		GetCode(root.right, start + "1", code, c);			
			
	}
	
		
	public void Compress() throws IOException {
		/** Calculate The Probabilities**/
		String data = read();
		int len = 0;
		int freq[] = new int [data.length()];
		String s = "";
		
		for (int i = 0; i < data.length(); i++) {
			if (s.contains((data.charAt(i) + "")))
				continue;
			
			float p = 1;
			s += data.charAt(i);
			
			for (int d = i + 1; d < data.length(); d++) {
				if (data.charAt(i) == data.charAt(d))
					p++;
			}
			//System.out.println(Math.round((p / data.length())*100));
			freq[len++] = (Math.round((p / data.length())*100));
		}
		
		/**-------------------------------------------**/
		
		/** Generate a Prefix Conditional Code **/
		PriorityQueue<Node> q 
        = new PriorityQueue<Node>(len, new comp());
		
		for (int i = 0; i < len; i++) {
			Node n = new Node();
			
			n.c = s.charAt(i);
			n.prob = freq[i];
			
			n.left = null;
			n.right = null;
			
			q.add(n);
		}
		
		Node root = new Node();
		
		while(q.size() > 1) {
			Node x = q.poll();
			Node y = q.poll();
			
			Node z = new Node();
			z.prob = x.prob + y.prob;
			z.c = '#';
			
			z.left = x;
			z.right = y;
			
			root = z;
			
			q.add(z);
		}
		
		ArrayList<String> code = new ArrayList<String>();
		ArrayList<Character> c = new ArrayList<Character>();
		
		GetCode(root, "", code, c);
		/**-------------------------------------------**/
		
		Writer file = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("C:\\Users\\amr_x\\Desktop\\Compressed.txt"), "utf-8"));
		
		System.out.println("Dic:");
		for (int i = 0; i < len; i++){
			System.out.print(c.get(i) + " : ");
			System.out.println(code.get(i));
			
			file.write(c.get(i));
			file.write(code.get(i) + "");
		}
		file.write('#');
		
		for(int i = 0; i < data.length(); i++) {
			int idx = c.indexOf(data.charAt(i));
			//int msk = Integer.parseInt(code.get(idx), 2);
			// file.write((char)msk);
			file.write(code.get(idx));
		}

		for (int i = 0; i < data.length(); i++){
			int idx = c.indexOf(data.charAt(i));
			System.out.print(code.get(idx) + " ");
		}
		System.out.println();
		
				
		file.close();
		
	}
	
	public void Decompress()throws IOException {	
		String data = read();
		
		ArrayList<String> c = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();
		
		int x = 0;
		String t = "";
		while (data.charAt(x) != '#') {
			if(Character.isLetter(data.charAt(x))) {
				if(x != 0) {
					code.add(t);
					t = "";
				}
				c.add(data.charAt(x)+"");
			}
			else {
				t += data.charAt(x);
			}
			x++;
		}
		code.add(t);
		t = "";
		for (int i = 0; i < code.size(); i++)
			System.out.println(c.get(i) + ": " + code.get(i));

		
		Writer file = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("C:\\Users\\amr_x\\Desktop\\Decompressed.txt"), "utf-8"));
		
		for (int i = x + 1; i < data.length(); i++) {
			t += data.charAt(i);
			int idx = code.indexOf(t);
			
			if(idx != -1) {
				file.write(c.get(idx));
				t = "";
			}
			
		}
		
		file.close();
	}

}