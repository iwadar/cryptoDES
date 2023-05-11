import java.nio.charset.StandardCharsets;

public class Main {

    public static final byte NUMBER_BIT_IN_BYTE = 8;
    //  начальная перестановка
    public static final int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36,
            28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32,
            24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19,
            11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

    // последняя перестановка
    public static final int[] inverseIP = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47,
            15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13,
            53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51,
            19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17,
            57, 25 };
    
    // функция расширения вектора R
    public static final int[] E = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8,
            9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,
            1 };

    // перестановка P в сети Фейстеля
    public static final int[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5,
            18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25 };

    // таблицы для генерации ключей
    // 64 бит -> 56 бит
    public static final int[] KE_1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34,
            26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63,
            55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53,
            45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };

    // сдвиг
    public static final int[] KShift = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

    // 56 -> 48 бит
    public static final int[] KE_2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29,
            32 };

    // S-преобразования
    private static final int[][][] SBlocks = {
            { 		{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                    { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                    { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                    { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
            },
            { 		{ 15, 1, 8, 14, 6, 11, 3, 2, 9, 7, 2, 13, 12, 0, 5, 10 },
                    { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                    { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                    { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
            },
            { 		{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                    { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                    { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                    { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
            },
            { 		{ 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                    { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                    { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                    { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
            },
            { 		{ 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                    { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                    { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                    { 11, 8, 12, 7, 1, 14, 2, 12, 6, 15, 0, 9, 10, 4, 5, 3 }
            },
            { 		{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                    { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                    { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                    { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }

            },
            { 		{ 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                    { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                    { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                    { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }

            },
            { 		{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                    { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                    { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                    { 2, 1, 14, 7, 4, 10, 18, 13, 15, 12, 9, 0, 3, 5, 6, 11 }

            } };
    // error can be here
    public static byte[] permutationBits(byte[] inputArray, int[] PBlock)
    {
        byte[] resultArray = new byte[(PBlock.length - 1)/ NUMBER_BIT_IN_BYTE + 1];
        int position, bit;
        for (int i = 0; i < PBlock.length; i++)
        {
            position = PBlock[i] - 1; //  - 1? потому что нумерация в таблице начинается с 1?
            bit = extractBitFromArray(inputArray, position);
            setBitIntoArray(resultArray, i, bit);
        }
        return resultArray;
    }

    // error can be here
    public static int extractBitFromArray(byte[] inputArray, int position)
    {
        int positionByte = position / NUMBER_BIT_IN_BYTE;
        int positionBit = position % NUMBER_BIT_IN_BYTE;
        return inputArray[positionByte] >>> (NUMBER_BIT_IN_BYTE - (positionBit + 1)) & 0x0001;
    }

    // error can be here
    public static void setBitIntoArray(byte[] inputArray, int position, int bit)
    {
        int positionByte = position / NUMBER_BIT_IN_BYTE;
        int positionBit = position % NUMBER_BIT_IN_BYTE;

        byte oldBit = inputArray[positionByte];
        oldBit = (byte) (((0xFF7F >> positionBit) & oldBit) & 0x00FF);
        byte newByte = (byte) ((bit << (8 - (positionBit + 1))) | oldBit);
        inputArray[positionByte] = newByte;
    }

    // на вход должен идти массив, который уже разбит на блоки по 6 бит
    public static byte[] replaceUsingSBlock(byte[] inputArray)
    {
        inputArray = separateArrayToBlocks(inputArray, 6);
        byte[] resultArray = new byte[inputArray.length / 2];
        int halfByte = 0;
        int row = 0, column = 0;
        for (int i = 0; i < inputArray.length; i++)
        {
            // зачем умножили на 2?
            row = 2 * (inputArray[i] >> 7 & 0x0001) + (inputArray[i] >> 2 & 0x0001);
            column = inputArray[i] >> 3 & 0x000F;
            if (i % 2 == 0)
            {
                halfByte = SBlocks[i][row][column];
            }
            else
            {
                // почему мы умножаем на 16?
                resultArray[i / 2] = (byte) (16 * halfByte + SBlocks[i][row][column]);
            }
        }
        return resultArray;
    }

    public static byte[] separateArrayToBlocks(byte[] inputArray, int sizeBlock)
    {
        int numberOfBytes = (inputArray.length * NUMBER_BIT_IN_BYTE - 1) / sizeBlock + 1;
        byte[] resultArray = new byte[numberOfBytes];
        int bit;
        for (int i = 0; i < numberOfBytes; i++)
        {
            for (int j = 0; j < sizeBlock; j++)
            {
                bit = extractBitFromArray(inputArray, sizeBlock * i + j);
                setBitIntoArray(resultArray, NUMBER_BIT_IN_BYTE * i + j, bit);
            }
        }
        return resultArray;
    }

    public static byte[] extractArrayBits(byte[] inputArray, int startPosition, int length)
    {
        byte[] resultArray = new byte[(length - 1) / NUMBER_BIT_IN_BYTE + 1];
        int bit;
        for (int i = 0; i < length; i++)
        {
            bit = extractBitFromArray(inputArray, startPosition + i);
            setBitIntoArray(resultArray, i, bit);
        }
        return resultArray;
    }
    public static byte[] concateBitArray(byte[] arrayFirst, int lenFirst, byte[] arraySecond, int lenSecond)
    {
        byte[] resultArray = new byte[(lenFirst + lenSecond - 1) / Main.NUMBER_BIT_IN_BYTE + 1];
        int j = 0;
        int bit;
        for (int i = 0; i < lenFirst; i++)
        {
            bit = Main.extractBitFromArray(arrayFirst, i);
            Main.setBitIntoArray(resultArray, i, bit);
            j += 1;
        }
        for (int i = 0; i < lenSecond; i++)
        {
            bit = Main.extractBitFromArray(arraySecond, i);
            Main.setBitIntoArray(resultArray, j, bit);
            j += 1;
        }
        return resultArray;
    }

    public static byte[] XOR(byte[] a, byte[] b)
    {
        byte[] resultArray = new byte[a.length];
        for (int i = 0; i < a.length; i++)
        {
            resultArray[i] = (byte) (a[i] ^ b[i]);
        }
        return resultArray;
    }

    public static void main(String[] args)
    {
        String text = "Dasha, h";
        String key = "qwhv74^E";
//        bnew KeyExpansion();
        DESCryption des = new DESCryption(new EncryptTransformation(), new KeyExpansion());
        des.setKey(key.getBytes());


        byte[] desEncrypt = des.encrypt(text.getBytes());
        System.out.println(new String(desEncrypt, StandardCharsets.UTF_8));
        byte[] desDecrypt = des.decrypt(desEncrypt);
        System.out.println(new String(desDecrypt, StandardCharsets.UTF_8));


//        System.out.printf(new String(tmp2, StandardCharsets.UTF_8));
//        System.out.println("Hello world!");
    }
}