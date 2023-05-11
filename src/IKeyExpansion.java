public interface IKeyExpansion
{
    public byte[][] generateRoundKeys(byte[] key);
    public byte[] shiftBits(byte[] inputArray, int len, int shift);
//    public byte[] shiftBits(byte[] inputArray, int shift, int r);
}
