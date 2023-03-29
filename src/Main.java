import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
public class Main {
    private static Cipher encrypt;
    //şifre çözmek için şifre sınıfının bir örneğini oluşturdum
    private static Cipher decrypt;
    //başlatma vektörü
    private static final byte[] initialization_vector = { 22, 33, 11, 44, 55, 99, 66, 77 };
    public static void main(String[] args)
    {//şifrelemek istediğimiz dosyanın yolu
        String textFile = "C:/Users/hudak/OneDrive/Masaüstü/DemoData.txt";
//çıktı olarak aldığımız şifreli dosyanın yolu
        String encryptedData = "C:/Users/hudak/OneDrive/Masaüstü/EncryptedData.txt";
// çıktı olarak aldığımız şifresi çözülmüş dosyanın yolu
        String decryptedData = "C:/Users/hudak/OneDrive/Masaüstü/DecryptedData.txt";
        try
        {
//KeyGenerator sınıfını kullanarak anahtar oluşturuyorum
            SecretKey scrtkey = KeyGenerator.getInstance("DES").generateKey();
            AlgorithmParameterSpec aps = new IvParameterSpec(initialization_vector);
//şifreleme metodunu çağırıyorum
            encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
            encrypt.init(Cipher.ENCRYPT_MODE, scrtkey, aps);
//şifreyi çözme metodunu çağırıyoruz
            decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
            decrypt.init(Cipher.DECRYPT_MODE, scrtkey, aps);
            encryption(new FileInputStream(textFile), new FileOutputStream(encryptedData));
            decryption(new FileInputStream(encryptedData), new FileOutputStream(decryptedData));
//program başarılı bir şekilde çalışırsa  aşağıda ki ifadeyi yazdırır
            System.out.println("Şifreli ve şifresi çözülmüş dosyalar başarıyla oluşturuldu.");
        }
//catch bloğunu kullanarak bir veya birden fazla hatada yakalıyoruz
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e)
        {
//İstisnai bi durum varsa hatayı yazdırıyor
            e.printStackTrace();
        }
    }
    //Şifreleme metodu
    private static void encryption(InputStream input, OutputStream output)
            throws IOException
    {
        output = new CipherOutputStream(output, encrypt);
        writeBytes(input, output);
    }
    //Şifreyi çözme metodu
    private static void decryption(InputStream input, OutputStream output)
            throws IOException
    {
        input = new CipherInputStream(input, decrypt);
        writeBytes(input, output);
    }
    private static void writeBytes(InputStream input, OutputStream output)
            throws IOException
    {
        byte[] writeBuffer = new byte[512];
        int readBytes = 0;
        while ((readBytes = input.read(writeBuffer)) >= 0)
        {
            output.write(writeBuffer, 0, readBytes);
        }
        output.close();
        input.close();
    }
}
