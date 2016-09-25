package assistants;

public class SSPEncryptor {
	private static int[] e = {53, 75, 70, 65, 72, 20, 53, 164, 65, 61, 161, 79, 20, 50, 61, 73, 73, 77, 165, 72, 64};
	
	public static String encriptMe(String ssp) {
		int[] p = ssp.chars().toArray();
		int[] n = new int[p.length+1];
		n[0] = (int)(Math.random() * 16);
		for(int i = 0, s = n[0]; i < p.length; i++,s++) {
			if(s >= e.length) {s = 0;}
			int d = Math.abs(p[i] - e[s]);
			n[p.length-i] = d + p.length;
		}
		int h = n[0];
		n[0] = n[3];
		n[3] = h;
		String c = "";
		for(int a = 0; a < n.length; a++) {
			String t = Integer.toHexString(n[a]);
			if(t.length() == 1 && a != 3) {
				t = "0" + t;
			}
			c += t;
		}
		return c;
	}
	
	public static boolean test(String attempt, String ssp) {
		int[] p = attempt.chars().toArray();
		int[] n = new int[p.length+1];
		n[0] = Integer.parseInt(ssp.substring(6, 7), 16);
		for(int i = 0, s = n[0]; i < p.length; i++,s++) {
			if(s == e.length) {s = 0;}
			int d = Math.abs(p[i] - e[s]);
			n[p.length-i] = d + p.length;
		}
		int h = n[0];
		n[0] = n[3];
		n[3] = h;
		String c = "";
		for(int a = 0; a < n.length; a++) {
			String t = Integer.toHexString(n[a]);
			if(t.length() == 1 && a != 3) {
				t = "0" + t;
			}
			c += t;
		}
		return c.equals(ssp);
	}

}
