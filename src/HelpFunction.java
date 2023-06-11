import java.util.Random;
import java.util.stream.Collectors;

public class HelpFunction {
    public static byte[] getArray64(byte[] initArray, int startIndex)
    {
        byte[] copy = new byte[8];
        System.arraycopy(initArray, startIndex, copy, 0, 8);
        return copy;
    }

    public static byte[] deletePadding(byte[] input)
    {
        int paddingLength = input[input.length-1];
        byte[] tmp = new byte[input.length - paddingLength];
        System.arraycopy(input, 0, tmp, 0, tmp.length);
        return tmp;
    }

    public static byte[] XORByteArray(byte[] first, byte[] second)
    {
        if (first.length != second.length)
        {
            System.out.println("ERROR XORByteArray(byte[] first, byte[] second): Array have to be same length");
            return new byte[0];
        }
        byte[] result = new byte[first.length];
        for (int i = 0; i < first.length; i++)
        {
            result[i] = (byte) (first[i] ^ second[i]);
        }
        return result;
    }


    public static String generateRandomString(int size)
    {
        String symbols = "abcdefghijklmnopqrstuvwxyz123456789";
        return new Random().ints(size, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
