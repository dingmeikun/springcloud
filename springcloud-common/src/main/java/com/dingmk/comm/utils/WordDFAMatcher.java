package com.dingmk.comm.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <b>字符串匹配类(DFA实现)</b><br>
 * 主要用于给定字符串与其它大量关键字的前缀/后缀匹配 及 关键字过滤
 * 
 * @author Tony.Lau
 * @blog https://my.oschina.net/xcafe
 * @createTime 2017-08-20
 * @updateBy Tony.Lau
 * @updateTime 2018-04-07
 */
public class WordDFAMatcher {
    // The first Node for DFA.
    private final Node startNode = new Node('0');

    public WordDFAMatcher() {
    }

    public WordDFAMatcher(Set<String> set) {
        if (null == set || set.isEmpty()) {
            return;
        }
        addAll(set);
    }

    public void add(String word) {
        if (null == word || word.isEmpty()) {
            return;
        }
        char[] chars = word.toCharArray();
        int charsLen = chars.length;
        int last = charsLen - 1;

        Node parentNode = startNode;
        for (int i = 0; i < charsLen; i++) {
            char c = chars[i];
            if (i == last) {
                parentNode = parentNode.put(new Node(c, word));
            } else {
                parentNode = parentNode.put(new Node(c));
            }
        }
    }

    /**
     * 400万长度为10的字符串，构建DFA约4097毫秒
     * 
     * @param set
     */
    public void addAll(Set<String> set) {
        if (null == set || set.size() == 0) {
            return;
        }

        for (String word : set) {
            add(word);
        }
    }

    /**
     * <b>给定一段文本，查找其中包含的关键字列表</b><br>
     * 性能：400万个长度为10的关键字，使用给定的长度为50的文本字符串 获取其中包含的关键字
     * "baidu.com"和"qq.com"，循环1000万次，耗时为10348毫秒<br>
     * 包含匹配（主要用于关键字过滤） 最坏时间复杂度：O(m * n) m为给定文本段长度， n为关键字长度
     * 
     * @param txt
     *            待检测过滤的文本段
     * @param minMatchType
     * @return 所有匹配的字符串列表
     */
    public Set<FindWord> contains(String txt, boolean minMatchType) {
        if (null == txt || txt.isEmpty()) {
            return Collections.emptySet();
        }

        if (null == startNode.table) {
            return Collections.emptySet();
        }

        char[] chars = txt.toCharArray();
        int charsLen = chars.length;

        Set<FindWord> set = new HashSet<FindWord>();
        for (int i = 0; i < charsLen; i++) {
            Node[] curTab = startNode.table;
            Node f = getNode(chars, i, charsLen, curTab, minMatchType);
            if (null != f) {
                int end = i + f.value.length() - 1;
                FindWord keyWord = new FindWord(i, end, f.value);
                i = end;
                set.add(keyWord);
            }
        }

        return set;
    }

    /**
     * <b>给定一个字符串，顺序查找所有匹配的字符串</b><br>
     * 
     * @param word
     *            待匹配的字符串，默认正向查找，不限定最大返回数量
     * @return 所有匹配的字符串列表
     */
    public List<String> findMatchList(String word) {
        return findMatchList(word, false, false, 0);
    }

    /**
     * <b>给定一个字符串，顺序查找所有匹配的字符串</b><br>
     * 
     * @param word
     *            带匹配的字符串
     * @param reverse
     *            是否反转查找
     * @param isLimit
     *            是否限定最大返回结果数量
     * @param maxSize
     *            限定的最大返回数量，isLimit==false时此参数无效
     * @return 所有匹配的字符串列表
     */
    public List<String> findMatchList(String word, boolean reverse, boolean isLimit, int maxSize) {
        if (word == null || word.isEmpty()) {
            return new ArrayList<>();
        }

        char[] chars = word.toCharArray();
        int charsLen = chars.length;

        Node[] curTab = startNode.table;
        if (curTab == null) {
            return new ArrayList<>();
        }

        if (reverse) {
            return reverseGetNodeList(chars, charsLen, curTab, isLimit, maxSize);
        }

        return getNodeList(chars, charsLen, curTab, isLimit, maxSize);
    }

    private List<String> reverseGetNodeList(char[] chars, int charsLen, Node[] curTab, boolean isLimit, int maxSize) {
        List<String> values = new ArrayList<>();
        if (isLimit) {
            values = new ArrayList<>(maxSize * 2);
        }
        for (int i = charsLen - 1, j = 0; i >= 0; i--) {
            char c = chars[i];
            int index = c & (curTab.length - 1);
            Node e = curTab[index];
            if (null == e || c != e.c) {
                return values;
            }
            if (e.value != null) {
                if (isLimit && ++j > maxSize) {
                    return values;
                }
                values.add(e.value);
            }
            if (null == (curTab = e.table)) {
                return values;
            }
        }
        return values;
    }

    private List<String> getNodeList(char[] chars, int charsLen, Node[] curTab, boolean isLimit, int maxSize) {
        List<String> values = new ArrayList<>();
        if (isLimit) {
            values = new ArrayList<>(maxSize * 2);
        }
        for (int i = 0, j = 0; i < charsLen; i++) {
            char c = chars[i];
            int index = c & (curTab.length - 1);
            Node e = curTab[index];
            if (null == e || c != e.c) {
                return values;
            }
            if (e.value != null) {
                if (isLimit && ++j > maxSize) {
                    return values;
                }
                values.add(e.value);
            }
            if (null == (curTab = e.table)) {
                return values;
            }
        }
        return values;
    }

    /**
     * <b>给定一个字符串，前缀匹配关键字</b><br>
     * 默认最长匹配，默认不反转字符串
     * 
     * @param word
     *            待匹配文本
     * @return
     */
    public String find(String word) {
        Node node = getNode(word, false, false);
        return null == node ? null : node.value;
    }

    /**
     * <b>给定一个字符串，前缀或后缀匹配关键字</b><br>
     * 性能：400万个长度为10的字符串，使用给定字符串 匹配关键字 "baidu.com" ，循环1000万次，耗时为973毫秒<br>
     * 时间复杂度：O(n) n为关键字长度，与给定字符串的长度无关
     * 
     * @param word
     *            待匹配文本
     * @param minMatchType
     *            是否最短匹配
     * @param reverse
     *            是否反转字符串匹配
     * @return
     */
    public String find(String word, boolean minMatchType, boolean reverse) {
        Node node = getNode(word, minMatchType, reverse);
        return null == node ? null : node.value;
    }

    /**
     * <b>给定一个字符串，前缀匹配列表</b><br>
     * 
     * @param word
     *            给定前缀
     * @param isLimit
     *            是否限定最大返回数量
     * @param maxSize
     *            最大返回数量，isLimit为false时无意义
     * @return 返回查找到的号码列表
     * @updateTime 2018-04-07
     */
    public List<String> findList(String word, boolean isLimit, int maxSize) {
        return getValueList(word, isLimit, maxSize);
    }

    private List<String> getValueList(String word, boolean isLimit, int maxSize) {
        if (word == null || word.length() == 0) {
            return null;
        }

        Node[] curTab = startNode.table;
        if (curTab == null) {
            return null;
        }

        char[] chars = word.toCharArray();
        return getValueList(chars, curTab, isLimit, maxSize);
    }

    private List<String> getValueList(char[] chars, Node[] curTab, boolean isLimit, int maxSize) {
        Node[] tempTab = curTab;
        int length = chars.length;
        for (int i = 0; i < length; i++) {
            char c = chars[i];
            int index = c & (tempTab.length - 1);
            Node e = tempTab[index];
            if (null == e) {
                return null;
            }

            if (c == e.c) {
                tempTab = e.table;
            }
        }

        if (null == tempTab) {
            return null;
        }

        List<String> values = new ArrayList<>();
        getValueList(tempTab, values, isLimit, maxSize);
        return values;
    }

    private void getValueList(Node[] tempTab, List<String> values, boolean isLimit, int maxSize) {
        if (isLimit && values.size() >= maxSize) {
            return;
        }

        for (Node e : tempTab) {
            if (null != e) {
                if (null != e.value) {
                    values.add(e.value);
                }
                Node[] curTab = e.table;
                if (null != curTab) {
                    getValueList(curTab, values, isLimit, maxSize);
                }
            }
        }

    }

    private Node getNode(String word, boolean minMatchType, boolean reverse) {
        if (word == null || word.length() == 0) {
            return null;
        }

        Node[] curTab = startNode.table;
        if (curTab == null) {
            return null;
        }

        char[] chars = word.toCharArray();
        int charsLen = chars.length;

        if (reverse) {
            return reverseGetNode(chars, 0, charsLen, curTab, minMatchType);
        }

        return getNode(chars, 0, charsLen, curTab, minMatchType);
    }

    private Node reverseGetNode(char[] chars, int start, int charsLen, Node[] curTab, boolean minMatchType) {
        Node f = null;
        for (int i = charsLen - 1; i >= start; i--) {
            char c = chars[i];
            int index = c & (curTab.length - 1);
            Node e = curTab[index];
            if (null == e) {
                return f;
            }

            boolean accept = false;
            while (null != e) {
                if (c == e.c) {
                    accept = true;
                    // 如果 c 与当前节点的值一致
                    // 如果当前节点的符号为结束符号，待返回值置为当前节点的值
                    if (e.value != null) {
                        f = e;
                        // 如果已经找到匹配对象，且当前设定为最短匹配，返回当前值
                        // （没有找到则继续对剩余字符进行匹配，直至所有字符匹配完毕）
                        if (minMatchType) {
                            return f;
                        }
                    }

                    if (null == (curTab = e.table)) {
                        return f;
                    }
                    break;
                }
                e = e.next;
            }
            if (!accept) {
                break;
            }
        }
        return f;
    }

    private Node getNode(char[] chars, int start, int charsLen, Node[] curTab, boolean minMatchType) {
        Node f = null;
        for (int i = start; i < charsLen; i++) {
            char c = chars[i];
            int index = c & (curTab.length - 1);
            Node e = curTab[index];
            if (null == e) {
                return f;
            }

            boolean accept = false;
            while (null != e) {
                if (c == e.c) {
                    accept = true;
                    // 如果 c 与当前节点的值一致
                    // 如果当前节点的符号为结束符号，待返回值置为当前节点的值
                    if (e.value != null) {
                        f = e;
                        // 如果已经找到匹配对象，且当前设定为最短匹配，返回当前值
                        // （没有找到则继续对剩余字符进行匹配，直至所有字符匹配完毕）
                        if (minMatchType) {
                            return f;
                        }
                    }

                    if (null == (curTab = e.table)) {
                        return f;
                    }
                    break;
                }
                e = e.next;
            }
            if (!accept) {
                break;
            }
        }
        return f;
    }

    /**
     * 移除关键字
     * 
     * @param word
     */
    public void remove(String word) {
        remove(word, false, false);
    }

    /**
     * 移除关键字
     * 
     * @param word
     * @param minMatchType
     * @param reverse
     */
    public void remove(String word, boolean minMatchType, boolean reverse) {
        Node node = getNode(word, minMatchType, reverse);
        if (null != node) {
            node.value = null;
        }
    }

    /**
     * 清除全部关键字
     */
    public void clear() {
        this.startNode.table = null;
    }

    public boolean isEmpty() {
        return this.startNode.cap == 0;
    }

    private static class Node {
        private final char c;
        private String value;
        private int cap = 0; // 容量
        private Node[] table;
        private Node next;

        public Node put(Node node) {
            int len = 0;
            if (table == null || (len = table.length) == 0) {
                len = 2;
                table = new Node[len];
            }

            if (cap == len) {
                reSize();
                len = table.length;
            }

            int index = node.c & (len - 1);
            Node e = table[index];
            // 如果当前数组中对应位置头结点为空
            if (e == null) {
                table[index] = node;
                cap++;
                return node;
            }

            // 循环查找首节点及其后继节点
            // 如果char值相同，返回该节点；
            // 如果char值不同，将节点添加到链表末尾；
            while (e != null) {
                if (node.c == e.c) {
                    // 如果为结束状态，当前节点的字符串保存为传入的字符串
                    if (node.value != null) {
                        e.value = node.value;
                    }
                    return e;
                }

                if (e.next == null) {
                    e.next = node;
                    cap++;
                    return node;
                }

                e = e.next;
            }

            return null;
        }

        private Node[] reSize() {
            int len = table.length;
            int newLen = len * 2;
            int mod = newLen - 1;
            Node[] newTab = new Node[newLen];

            for (int i = 0; i < len; i++) {
                // oldTab元素转存 newTab
                Node e = table[i];
                while (e != null) {
                    int index = e.c & mod;
                    Node h = newTab[index];
                    if (h == null) {
                        newTab[index] = e;
                        e = e.next;
                        newTab[index].next = null;
                    } else {
                        while (h != null) {
                            Node t = h.next;
                            if (t == null) {
                                h.next = e;
                                e = e.next;
                                h.next.next = null;
                            }
                            h = t;
                        }
                    }
                }
            }

            return (table = newTab);
        }

        public Node(char c) {
            this(c, null);
        }

        public Node(char c, String value) {
            this.c = c;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node [c=" + c + ", value=" + value + ", cap=" + cap + ", next=" + next + ", table="
                    + Arrays.toString(table) + "]";
        }

    }

    public static class FindWord {
        public final int start;
        public final int end;
        public final String value;

        public FindWord(int start, int end, String value) {
            this.start = start;
            this.end = end;
            this.value = value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + end;
            result = prime * result + start;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            FindWord other = (FindWord) obj;
            if (end != other.end) {
                return false;
            }
            if (start != other.start) {
                return false;
            }
            if (value == null) {
                if (other.value != null) {
                    return false;
                }
            } else if (!value.equals(other.value)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "FindWord [start=" + start + ", end=" + end + ", value=" + value + "]";
        }
    }

}
