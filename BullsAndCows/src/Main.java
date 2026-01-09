public class Main {
    public static void main(String[] args) {
        ListNode list1 = new ListNode(1);
        ListNode list2 = new ListNode(2);
        ListNode list3 = new ListNode(3);
        list1.next = list2;
        list2.next = list3;

        ListNode dummy = new ListNode(0, list3);

        ListNode cur = new ListNode();
        cur = dummy.next;

        Reserve(list1);

        while (cur != null) {
            System.out.println(cur.val);
            cur = cur.next;
        }
    }

    public static void Reserve(ListNode head) {
        ListNode dummy = new ListNode(0, head);

        ListNode prev = new ListNode();
        prev = dummy;
        ListNode cur = new ListNode();
        cur = head;

        while (cur.next != null) {
            ListNode next = cur.next;
            cur.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }
    }
}
