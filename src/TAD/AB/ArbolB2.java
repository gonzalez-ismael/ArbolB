/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TAD.AB;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ismael
 */
public class ArbolB2 {
    // Java Code
    // Deletes value from the node
    public NodoB del(int val, NodoB root) {
        NodoB temp;
        if (!delhelp(val, root)) {
            System.out.println(
                    String.format("Value %d not found.", val));
        } else {
            if (root.getCantClaves() == 0) {
                temp = root;
                root = root.getHijoEn(0);
                temp = null;
            }
        }
        return root;
    }

    // GFG
// Java Code
// Helper function for del()
    private boolean delhelp(int val, NodoB root) {
        AtomicInteger i = new AtomicInteger();
        boolean flag;
        if (root == null) {
            return false;
        } else {
            // Again searches for the node
            flag = searchnode(val, root, i);
            // if flag is true
            if (flag) {
                if (root.getHijoEn(i.get() - 1) == null) {
                    clear(root, i.get());
                } else {
                    copysucc(root, i.get());
                    // delhelp() is called recursively
                    flag = delhelp(root.getClaveEn(i.get()), root.getHijoEn(i.get()));
                    if (flag == false) {
                        System.out.println(String.format("Value %d not found.", root.getClaveEn(i.get())));
                    }
                }
            } else {
                // Recursion
                flag = delhelp(val, root.getHijoEn(i.get()));
            }
            if (root.getHijoEn(i.get()) != null) {
                if (root.getHijoEn(i.get()).getCantClaves() < root.getM() / 2) {
                    restore(root, i.get());
                }
            }
            return flag;
        }
    }

    // GFG Java Code
    // Removes the value from the
    // node and adjusts the values
    public static void clear(NodoB m, int k) {
        for (int i = k + 1; i <= m.getCantClaves(); i++) {
            m.setClaveEn(i - 1, m.getClaveEn(i));
            m.setHijoEn(i - 1, m.getHijoEn(i));
        }
        m.setCantClaves(m.getCantClaves() - 1);
    }

    // Copies the successor of the
    // value that is to be deleted
    public static void copysucc(NodoB m, int i) {
        NodoB temp = m.getHijoEn(i);
        while (temp.getHijoEn(0) != null) {
            temp = temp.getHijoEn(0);
        }
        m.setClaveEn(i, m.getClaveEn(0));
    }

    // Adjusts the node
    public static void restore(NodoB m, int i) {
        if (i == 0) {
            if (m.getHijoEn(1).getCantClaves() > m.getM() / 2) {
                leftshift(m, 1);
            } else {
                merge(m, 1);
            }
        } else {
            if (i == m.getCantClaves()) {
                if (m.getHijoEn(i - 1).getCantClaves() > m.getM() / 2) {
                    rightshift(m, i);
                } else {
                    merge(m, i);
                }
            } else {
                if (m.getHijoEn(i - 1).getCantClaves() > m.getM() / 2) {
                    rightshift(m, i);
                } else {
                    if (m.getHijoEn(i + 1).getCantClaves() > m.getM() / 2) {
                        leftshift(m, i + 1);
                    } else {
                        merge(m, i);
                    }
                }
            }
        }
    }

    // Java Code
    // Adjusts the values and children
    // while shifting the value from
    // parent to right child
    private static void rightshift(NodoB m, int k) {
        NodoB temp = m.getHijoEn(k);
        // Copying the nodes
        for (int i = temp.getCantClaves(); i > 0; i--) {
            temp.setClaveEn(i + 1, temp.getClaveEn(i));
            temp.setHijoEn(i + 1, temp.getHijoEn(i));
        }
        temp.setHijoEn(1, temp.getHijoEn(0));
        temp.setCantClaves(temp.getCantClaves() + 1);
        temp.setClaveEn(1, k);
        
        temp = m.getHijoEn(k - 1);
        m.setClaveEn(k, temp.getClaveEn(temp.getCantClaves()));
        m.getHijoEn(k).setHijoEn(0, temp.getHijoEn(temp.getCantClaves()));
        temp.setCantClaves(temp.getCantClaves() - 1);
    }

    // Adjusts the values and children
    // while shifting the value from
    // parent to left child
    private static void leftshift(NodoB m, int k) {
        NodoB temp = m.getHijoEn(k - 1);
        temp.setCantClaves(temp.getCantClaves() + 1);
        temp.setClaveEn(temp.getCantClaves(), m.getClaveEn(k));
        temp.setHijoEn(temp.getCantClaves(), m.getHijoEn(k).getHijoEn(0));
        
        temp = m.getHijoEn(k);
        m.setClaveEn(k, temp.getClaveEn(1));
        temp.setHijoEn(0, temp.getHijoEn(1));
        temp.setCantClaves(temp.getCantClaves() - 1);
        
        for (int i = 1; i <= temp.getCantClaves(); i++) {
            temp.setClaveEn(i, temp.getClaveEn(i + 1));
            temp.setHijoEn(i, temp.getHijoEn(i + 1));
        }
    }
    
    private static void merge(NodoB m, int k) {
        int i;
        NodoB temp1, temp2;
        
        temp1 = m.getHijoEn(k);
        temp2 = m.getHijoEn(k - 1);
        temp2.setCantClaves(temp2.getCantClaves() + 1);
        temp2.setClaveEn(temp2.getCantClaves(), m.getClaveEn(k));
        temp2.setHijoEn(temp2.getCantClaves(), m.getHijoEn(0));
        
        for (i = 0; i <= temp1.getCantClaves(); i++) {
            temp2.setCantClaves(temp2.getCantClaves() + 1);
            temp2.setClaveEn(temp2.getCantClaves(), temp1.getClaveEn(i));
            temp2.setHijoEn(temp2.getCantClaves(), temp1.getHijoEn(i));
        }
        
        for (i = k; i < m.getCantClaves(); i++) {
            m.setClaveEn(i, m.getClaveEn(i + 1));
            m.setHijoEn(i, m.getHijoEn(i + 1));
        }
        m.setCantClaves(m.getCantClaves()-1);
    }
    
    public boolean searchnode(Integer val, NodoB n, AtomicInteger pos) {
        // if val is less than node.value[1]
        if (val < n.getClaveEn(1)) {
            pos.set(0);
            return false;
        } // if the val is greater
        else {
            pos.set(n.getCantClaves());

            // check in the child array
            // for correct position
            while ((val < n.getClaveEn(pos.get())) && pos.get() > 1) {
                pos.set(pos.get() - 1);
            }
            
            return Objects.equals(val, n.getClaveEn(pos.get()));
        }
    }
}
