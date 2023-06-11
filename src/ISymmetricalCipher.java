public interface ISymmetricalCipher
{
    byte[] encrypt(byte[] inputArray);
    byte[] decrypt(byte[] inputArray);
    void setKey(byte[] key);
}
