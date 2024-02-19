package CLIENT;

import btl_qlptgt.controller.AccountController;
import btl_qlptgt.view.SignIn;

/**
 *
 * @author naman
 */
public class BTL_QLPTGT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        AccountController ac = new AccountController();
        ac.createAdminAccount();
        SignIn signIn = new SignIn();
        signIn.setLocationRelativeTo(null);
        signIn.setVisible(true);
    }
    
}
