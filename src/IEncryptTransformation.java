public interface IEncryptTransformation
{
    public byte[] encryptBlock(byte[] inputArray, byte[] roundKey);
}
