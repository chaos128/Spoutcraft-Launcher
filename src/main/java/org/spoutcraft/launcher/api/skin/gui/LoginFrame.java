package org.spoutcraft.launcher.api.skin.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JFrame;

import org.spoutcraft.launcher.api.skin.Skin;
import org.spoutcraft.launcher.api.util.Utils;

public abstract class LoginFrame extends JFrame {

	private static final long serialVersionUID = -2105611446626766230L;
	private Map<String, UserPasswordInformation> usernames = new HashMap<String, UserPasswordInformation>();
	private final Skin parent;

	public LoginFrame(Skin parent) {
		this.parent = parent;
		readSavedUsernames();
	}

	public List<String> getSavedUsernames() {
		return new ArrayList<String>(usernames.keySet());
	}

	public final boolean hasSavedPassword(String user) {
		return true;
	}

	public Skin getParentSkin() {
		return parent;
	}

	public void doLogin(String user) {
		if (!hasSavedPassword(user))
			throw new NullPointerException("There is no saved password for the user '" + user + "'");
		doLogin(user, null);
	}

	public void doLogin(String user, String pass) {

	}

	private void readSavedUsernames() {
		try {
			File lastLogin = new File(Utils.getWorkingDirectory(), "lastlogin");
			if (!lastLogin.exists())
				return;
			Cipher cipher = getCipher(2, "passwordfile");

			DataInputStream dis;
			if (cipher != null)
				dis = new DataInputStream(new CipherInputStream(new FileInputStream(lastLogin), cipher));
			else {
				dis = new DataInputStream(new FileInputStream(lastLogin));
			}

			try {
				while (true) {
					String user = dis.readUTF();
					boolean isHash = dis.readBoolean();
					if (isHash) {
						byte[] hash = new byte[32];
						dis.read(hash);
						usernames.put(user, new UserPasswordInformation(hash));
					} else {
						String pass = dis.readUTF();
						usernames.put(user, new UserPasswordInformation(pass));
					}
				}
			} catch (EOFException e) {
			}
			dis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeUsernameList() {
		try {
			File lastLogin = new File(Utils.getWorkingDirectory(), "lastlogin");

			Cipher cipher = getCipher(1, "passwordfile");

			DataOutputStream dos;
			if (cipher != null)
				dos = new DataOutputStream(new CipherOutputStream(new FileOutputStream(lastLogin), cipher));
			else {
				dos = new DataOutputStream(new FileOutputStream(lastLogin, true));
			}
			for (String user : usernames.keySet()) {
				dos.writeUTF(user);
				UserPasswordInformation info = usernames.get(user);
				dos.writeBoolean(info.isHash);
				if (info.isHash) {
					dos.write(info.passwordHash);
				} else {
					dos.writeUTF(info.password);
				}
			}
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Cipher getCipher(int mode, String password) throws Exception {
		Random random = new Random(43287234L);
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);

		SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(password.toCharArray()));
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(mode, pbeKey, pbeParamSpec);
		return cipher;
	}

	public abstract void init();

	private static final class UserPasswordInformation {
		public boolean isHash;
		public byte[] passwordHash = null;
		public String password = null;

		public UserPasswordInformation(String pass) {
			isHash = false;
			password = pass;
		}

		public UserPasswordInformation(byte[] hash) {
			isHash = true;
			passwordHash = hash;
		}
	}

}
