public interface IDESCryption
{
    byte[] encrypt(byte[] inputArray);
    byte[] decrypt(byte[] inputArray);
    void setKey(byte[] key);
}
