public class EncryptTransformation implements IEncryptTransformation
{
    // преобразование внутри сети Фейстеля
    @Override
    public byte[] encryptBlock(byte[] inputArray, byte[] roundKey)
    {
        byte[] resultArray = Main.permutationBits(inputArray, Main.E);
        resultArray = Main.XOR(resultArray, roundKey);
        resultArray = Main.replaceUsingSBlock(resultArray);
        resultArray = Main.permutationBits(resultArray, Main.P);
        return resultArray;
    }
}
