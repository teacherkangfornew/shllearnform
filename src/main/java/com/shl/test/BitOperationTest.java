package com.shl.test;

/**
 * &：按位与。
 * <p>
 * |：按位或。
 * <p>
 * ~：按位非。
 * <p>
 * ^：按位异或。(相同为0，否则为1)
 * <p>
 * <<：左位移运算符。
 * <p>
 * >>：右位移运算符。
 * <p>
 * <<<：无符号右移运算符
 */
public class BitOperationTest {

    public static void main(String[] args) {
        isOdd(10);
        isOdd(11);
        h();
        exp(10);
        h();
        divedeExp(2);
        h();
        deleteLastOne(1);
        deleteLastOne(0);
        deleteLastOne(-1);
        h();
        negate(10);
        negate(-4);
        h();
        turnNIsTrue(10, 5);
        turnNIsFalse(10, 4);
        h();
        getLastN(10, 3);
        getBeforeN(10, 1);
        h();
        getRightFirstOne(10);
        h();
        aa(10, 6, 10);

        h();
        isSameOpt(10, 0);
        isSameOpt(-1, 0);
    }

    // 判断x和y的符号是否相同：(a ^ b) >= 0
    static void isSameOpt(int a, int b) {
        if ((a ^ b) >= 0) {
            System.out.println(">>>>>..a b 符号相同");
        } else {
            System.out.println(">>>>>>>a b 符号 不同");
        }
    }

    // if(x == a) x = b; if(x == b) x = a：用异或运算符：x = a ^ b ^ x。
    static void aa(int a, int b, int x) {
        x = a ^ b ^ x;
        System.out.println(">>>>>>>>" + x);
    }

    // 只保留x右边第一个1，其他的全部置为0：x & (-x)
    static void getRightFirstOne(int x) {
        System.out.println(">>>>>>>>>>" + (x & (-x)));
    }

    // 取x前n位的值，后面的全部置为0：x & (~((1 << (32 - n)) - 1)) 或者 x & -(1 << (32 - n))
    static void getBeforeN(int x, int n) {
        System.out.println(">>>>>>" + (x & -(1 << (32 - n))));
    }

    // 取x最后n位的值：x&((1<<n)-1)
    static void getLastN(int x, int n) {
        System.out.println(">>>>>>>" + (x & ((1 << n) - 1)));
    }

    // 把x从右边数第n位变0：x&(~(1<<(n-1)))（n从1开始）
    static void turnNIsFalse(int x, int n) {
        System.out.println(">>>>>>>>>" + (x & ~(1 << (n - 1))));
    }

    // 把x从右边数第n位变1：x|(1<<(n-1))（n从1开始）
    static void turnNIsTrue(int x, int n) {
        System.out.println(">>>>>>>>>" + (x | (1 << (n - 1))));
    }

    // 求x的相反数： ~(x-1)或者~x+1
    static void negate(int n) {
        System.out.println(">>>>>>>>" + (~n + 1));
    }

    //消去x最后一位的1：x&(x-1)
    static void deleteLastOne(int n) {
        System.out.println(">>>" + (n & (n - 1)));
    }

    // x除以一个2的n次方的数：x>>n
    static void divedeExp(int n) {
        System.out.println(">>>>" + (n >> 1));
    }

    // x乘以一个2的n次方的数：x<<n
    static void exp(int n) {
        System.out.println(">>>>>>>>" + (n << 2));
    }

    // 判断x是奇数还是偶数：(x&1)==0
    static void isOdd(int n) {
        if ((n & 1) == 0) {
            System.out.println(">>>>偶数");
        } else {
            System.out.println(">>>>奇数");
        }
    }

    static void h() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}

