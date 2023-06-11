import java.util.Random;
import java.util.stream.Collectors;

public class DESCryption implements ISymmetricalCipher
{
    private byte[][] roundKeys;
    KeyExpansion keyExpansion;
    EncryptTransformation encryptTransformation;

    DESCryption(EncryptTransformation encryptTransformation, KeyExpansion keyExpansion)
    {
        this.encryptTransformation = encryptTransformation;
        this.keyExpansion = keyExpansion;
        this.roundKeys = keyExpansion.generateRoundKeys(generateRandomString(8).getBytes());
    }

    public static String generateRandomString(int size)
    {
        String symbols = "abcdefghijklmnopqrstuvwxyz123456789";
        return new Random().ints(size, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
    @Override
    public void setKey(byte[] key)
    {
        this.roundKeys = keyExpansion.generateRoundKeys(key);
    }
    private byte[] oneRoundEncrypt(byte[] inputArray, int round) {
        byte[] L = Main.extractArrayBits(inputArray, 0, Main.IP.length / 2);
        byte[] R = Main.extractArrayBits(inputArray, Main.IP.length / 2, Main.IP.length / 2);

        byte[] tempR = R;
        R = encryptTransformation.encryptBlock(R, roundKeys[round]);
        R = Main.XOR(L, R);
        L = tempR;

        return  Main.concateBitArray(R, Main.IP.length / 2, L, Main.IP.length / 2);
    }

    private byte[] oneRoundDecrypt(byte[] inputArray, int round)
    {
        byte[] L = Main.extractArrayBits(inputArray, 0, Main.IP.length / 2);
        byte[] R = Main.extractArrayBits(inputArray, Main.IP.length / 2, Main.IP.length / 2);
        byte[] tempL = L;

        L = encryptTransformation.encryptBlock(L, roundKeys[round]);
        L = Main.XOR(L, R);
        R = tempL;

        return Main.concateBitArray(R, Main.IP.length / 2, L, Main.IP.length / 2);
    }

    @Override
    public byte[] encrypt(byte[] inputArray)
    {
//        int lengthPadding = Main.NUMBER_BIT_IN_BYTE - inputArray.length % Main.NUMBER_BIT_IN_BYTE;
//        byte[] copyInputArrayWithPadding = new byte[inputArray.length + lengthPadding];
//        System.arraycopy(inputArray, 0, copyInputArrayWithPadding, 0, inputArray.length);
//        for (int i = 0; i < lengthPadding; i++)
//        {
//            copyInputArrayWithPadding[inputArray.length + i] = (byte)lengthPadding;
//        }

        byte[] block = new byte[Main.NUMBER_BIT_IN_BYTE];
        for (int i = 0; i < inputArray.length; i+=Main.NUMBER_BIT_IN_BYTE)
        {
            System.arraycopy(inputArray, i, block, 0, Main.NUMBER_BIT_IN_BYTE);
            block = Main.permutationBits(block, Main.IP);
            for (int k = 0; k < KeyExpansion.NUMBER_OF_ROUND_KEYS; k++)
            {
                block = oneRoundEncrypt(block, k);
            }
            block = Main.permutationBits(block, Main.inverseIP);
            System.arraycopy(block, 0, inputArray, i, Main.NUMBER_BIT_IN_BYTE);
        }
        return inputArray;

//        for (int i = 0; i < copyInputArrayWithPadding.length; i+=Main.NUMBER_BIT_IN_BYTE)
//        {
//            System.arraycopy(copyInputArrayWithPadding, i, block, 0, Main.NUMBER_BIT_IN_BYTE);
//            block = Main.permutationBits(block, Main.IP);
//            for (int k = 0; k < KeyExpansion.NUMBER_OF_ROUND_KEYS; k++)
//            {
//                block = oneRoundEncrypt(block, k);
//            }
//            block = Main.permutationBits(block, Main.inverseIP);
//            System.arraycopy(block, 0, copyInputArrayWithPadding, i, Main.NUMBER_BIT_IN_BYTE);
//        }
//        return copyInputArrayWithPadding;
    }

    @Override
    public byte[] decrypt(byte[] inputArray)
    {
        byte[] block = new byte[Main.NUMBER_BIT_IN_BYTE];

        for (int i = 0; i < inputArray.length; i+=Main.NUMBER_BIT_IN_BYTE)
        {
            System.arraycopy(inputArray, i, block, 0, Main.NUMBER_BIT_IN_BYTE);
            block = Main.permutationBits(block, Main.IP);
            for (int k = 0; k < KeyExpansion.NUMBER_OF_ROUND_KEYS; k++)
            {
                block = oneRoundEncrypt(block, 15 - k);
//                block = oneRoundDecrypt(block, 15 - k);
            }
            block = Main.permutationBits(block, Main.inverseIP);
            System.arraycopy(block, 0, inputArray, i, Main.NUMBER_BIT_IN_BYTE);
        }
//        inputArray = deletePadding(inputArray);
        return inputArray;
    }

}
