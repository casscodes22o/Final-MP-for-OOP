package mp3java2;

//Cassandra D. Cabral 1CSC ICS2606 
import java.util.*;
import java.io.*;

// = if user input, : for program output

public class MP3Java2 
{
    static Scanner scanner = new Scanner (System.in); 
    public static void main(String[] args) 
    {
        
        try
        {            
            //for account.txt
            File accFile = new File("accounts.txt");
            ObjectOutputStream outWrite = new ObjectOutputStream(new FileOutputStream(accFile));
            if(!accFile.exists())
                accFile.createNewFile();

            List<Account> accounts = new ArrayList<>(); //dupe
            List<Object> accInfo = new ArrayList<>();
            accInfo.add(new Account("111",'O','2',1));
            accInfo.add(new Account("567",'L','1',0));
            accInfo.add(new Account("456",'O','3',2));
            accInfo.add(new Account("123",'L','2',1));
            accInfo.add(new Account("890",'O','1',1));
            accInfo.add(new Account("786",'L','3',1));
            accInfo.add(new Account("057",'O','3',2));
            accInfo.add(new Account("945",'L','2',1));
            accInfo.add(new Account("655",'O','3',3));
            accInfo.add(new Account("897",'L','1',1));
            accInfo.add(new Account("801",'O','2',2));
            accInfo.add(new EndFile());
            
            for (Object account : accInfo)
            {
                outWrite.writeObject(account);
                if (account instanceof Account) 
                {
                    Account acc = (Account) account;
                    accounts.add(acc);
                }
            }   
            outWrite.flush();
            
            File ppl = new File ("mapMP3.txt");
            Map<String, Person> Permap = new HashMap<>();           
            BufferedReader br = new BufferedReader(new FileReader(ppl));
            
            String line;
            while ((line = br.readLine()) != null) 
            {              
                Person person = new Person();
                String[] div = line.split(" ");
                String ID = div[0];
                person.setFName(div[1]);
                person.setLName(div[2]);
                person.setAge(div[3]);
                person.setAddress(div[4]);
                Permap.put(ID, person);
            }
            
            FileWriter writer = new FileWriter(ppl);
            for (Map.Entry<String, Person> entry : Permap.entrySet())
            {
                String IDkey = entry.getKey();
                Person person = entry.getValue();
                writer.write(IDkey+" "+person.getFName()+" "+person.getLName()+" "+person.getAge()+" "+person.getAddress()+"\n");
            }
            
            //declare variables
            int choice, park, chedit; 
            String fname, lname, age, address, accID; 
            char occT, uniT;
       
            menuProg();
            do  
            {
                System.out.print("\nENTER NO. OF YOUR CHOICE (Main Menu) = ");
                choice = valMenu();
                switch (choice)
                {
                    case 1 : //add acc
                        //person Person(String fname, String lname, String age, String address)
                        System.out.print("\nEnter your First name = ");
                        fname = scanner.nextLine(); 
                        System.out.print("Enter your Last name = ");
                        lname = scanner.nextLine();
                        System.out.print("Enter your Age = ");
                        age = valAge(); 
                        System.out.print("Enter your Address = ");
                        address = scanner.nextLine();
                        Person per = new Person (fname, lname, age, address);

                        //acc Account (String ID, char occuType, char unitType, int numPark)
                        System.out.print("Enter your ID = ");
                        accID = valID(accounts);
                        System.out.print("Enter Occupation Type (L/O) = ");
                        occT = valOcc();
                        System.out.print("Enter Unit Type (1/2/3) = ");
                        uniT = valUnit(); 
                        System.out.print("Enter number of parking space to occupy = ");
                        park = valPark(uniT);
                        Account acc = new Account (accID, occT, uniT, park); 
                        
                        //accInfo.add(acc); 
                        writer.write(accID + " " + per.toString() + "\n");
                        //writer.flush();
                        outWrite.writeObject(acc);
                        outWrite.writeObject(new EndFile());  //end of file
                        //outWrite.flush();
                        if (acc instanceof Account) 
                            accounts.add(acc);                        
                        Permap.put(accID, per);
                        break;
                    case 2 : //delete acc
                        System.out.println();
                        System.out.print("Enter KEY of Account You Want to DELETE = "); 
                            String keyID = scanner.nextLine();
                        
                        Iterator it = accounts.iterator();                       
                        while (it.hasNext()) 
                        {
                            Account ob = (Account) it.next();
                            if (Permap.containsKey(keyID) && (ob.getID()).equals(keyID))
                            {
                                System.out.printf("Deleting %s %s's Account...\n", 
                                    Permap.get(keyID).getFName(), Permap.get(keyID).getLName());
                                Permap.remove(keyID);
                                it.remove();
                                break;
                            } 
                        } 
                        break;
                    case 3 : //display an acc 
                        System.out.println();
                        System.out.print("Enter KEY of Account You Want to DISPLAY = "); 
                            keyID = scanner.nextLine();
                        System.out.println();
                        if (Permap.containsKey(keyID))
                        {
                            System.out.println("Account exists");
                            it = accounts.iterator();
                            while (it.hasNext()) 
                            {
                                Account ob = (Account) it.next();
                                if ((ob.getID()).equals(keyID))
                                {
                                    System.out.println("PERSONAL DETAILS");
                                    System.out.printf("First Name: %s \nLast Name: %s \nAge: %s \nAddress: %s", 
                                            Permap.get(ob.getID()).getFName(), Permap.get(ob.getID()).getLName(),
                                            Permap.get(ob.getID()).getAge(), Permap.get(ob.getID()).getAddress());
                                    System.out.println("\n\nACCOUNT DETAILS");
                                    System.out.printf("Account ID: %s \nOccupant Type: %s \nUnit Type: %s \nNo. of Parking Spaces: %d \nMonthly Dues: %.2f", 
                                            ob.getID(), ob.getOccuType(), ob.getUnitType(), ob.getNumPark(), calcMonthlyDue(ob.getUnitType(), ob.getOccuType(), ob.getNumPark()));
                                }                                    
                            }                           
                        }                           
                        else 
                            System.out.print("Account DOES NOT exist");    
                        System.out.println();
                        break;
                    case 4 : //edit an acc find key
                        System.out.println();
                        System.out.print("Enter KEY of Account You Want to EDIT = "); 
                            keyID = scanner.nextLine();
                        if (Permap.containsKey(keyID))
                        {
                            menuEdit(); 
                            System.out.print("\nENTER NO. OF YOUR CHOICE (Edit Account) = ");
                            chedit = valMenu(); 
                            switch (chedit)
                            {
                                case 1 : //fname
                                    System.out.print("EDIT your First name = ");
                                    fname = scanner.nextLine(); 
                                    Permap.get(keyID).setFName(fname);
                                    
                                    break;

                                case 2 : //lname
                                    System.out.print("EDIT your Last name = ");
                                    lname = scanner.nextLine();
                                    Permap.get(keyID).setLName(lname);
                                    break;
                                case 3 : //age
                                    System.out.print("EDIT your Age = ");
                                    age = valAge(); 
                                    Permap.get(keyID).setAge(age);
                                    break;
                                case 4 : //address
                                    System.out.print("EDIT your Address = ");
                                    address = scanner.nextLine();
                                    Permap.get(keyID).setAddress(address);
                                    break;
                                case 5 : //occ type
                                    System.out.print("EDIT Occupation Type (L/O) = ");
                                    occT = valOcc();
                                    it = accounts.iterator();
                                    while (it.hasNext()) 
                                    {
                                        Account ob = (Account) it.next();
                                        if ((ob.getID()).equals(keyID))
                                        {
                                            ob.setOccuType(occT);
                                        }                                    
                                    }     
                                    break;
                                case 6 : //unittype
                                    System.out.print("EDIT Unit Type (1/2/3) = ");
                                    uniT = valUnit(); 
                                    it = accounts.iterator();
                                    while (it.hasNext()) 
                                    {
                                        Account ob = (Account) it.next();
                                        if ((ob.getID()).equals(keyID))
                                        {
                                            ob.setUnitType(uniT);
                                        }                                    
                                    }
                                    break;
                                case 7 : //parking   
                                    it = accounts.iterator();
                                    System.out.print("Enter number of parking space to occupy = ");
                                    while (it.hasNext()) 
                                    {
                                        Account ob = (Account) it.next();
                                        if ((ob.getID()).equals(keyID))
                                        {
                                            //scanner.nextLine(); //flush
                                            park = valPark(ob.getUnitType()); //prameter the unit type
                                            ob.setNumPark(park);
                                        }                                    
                                    }
                                    break;
                                case 8 : 
                                    //nothing
                                    break;                          
                            }
                            /*
                            writer.write(keyID+" "+Permap.get(keyID).getFName()+" "+
                                            Permap.get(keyID).getLName()+" "+
                                            Permap.get(keyID).getAge()+" "+
                                            Permap.get(keyID).getAddress()+"\n");
                            */
                        }
                        else
                            System.out.print("Account DOES NOT exist\n");  
                        break;
                    case 5 : //search an acc find key
                        System.out.println();
                        System.out.print("Enter KEY of Account You Want to SEARCH = "); 
                            keyID = scanner.nextLine();
                        if (Permap.containsKey(keyID))
                        {
                            System.out.println("Account exists");
                        }
                        else 
                            System.out.println("Account DOES NOT exist");
                        break;
                    case 6 : //display all monthly dues
                        System.out.println();
                        double sum = 0, max = 0, min = 0, mad;
                        boolean first = true;
                        it = accounts.iterator(); 
                        
                        System.out.println("First Name\tLast Name\tOccupant\t\tUnit\t\tParking\t\tMonthly Dues");
                        while (it.hasNext())
                        {
                            Account ob = (Account) it.next();
                            mad = calcMonthlyDue(ob.getUnitType(), ob.getOccuType(), ob.getNumPark()); 
                            System.out.print(Permap.get(ob.getID()).getFName() + "\t\t" + Permap.get(ob.getID()).getLName() + "\t");
                            if (Permap.get(ob.getID()).getLName().equals("Ballesteros")) //i lowkey have ocd
                            {
                                System.out.printf("%s\t\t%s\t\t%s\t\t%.2f\n", ob.getOccuType(), ob.getUnitType(), ob.getNumPark(), mad);
                                continue;
                            }
                            System.out.printf("\t%s\t\t%s\t\t%s\t\t%.2f\n", ob.getOccuType(), ob.getUnitType(), ob.getNumPark(), mad); //dont show ID num for privacy reasons
                            if (first) //will only go here once, initialize
                            {
                                max = mad;
                                min = mad;
                                first = false;
                            }
                            else 
                            {
                                if (max < mad)
                                    max = mad;
                                if (min > mad)
                                    min = mad;
                            }
                            sum += mad;   
                        }
                        System.out.printf("\nSum of all MAD : %.2f\nMAX : %.2f \nMIN : %.2f\n",sum,max,min); 
                        
                        break;


                    case 7 : //display all acc
                        System.out.println("\nList of Accounts");
                        Collections.sort(accounts, new compareID());
                        it = accounts.iterator();
                        while (it.hasNext()) 
                        {
                            Object obj = it.next();
                            if (!(obj instanceof EndFile))
                                System.out.println(obj);
                        }
                        
                        Map<String, Person> Sortmap = new TreeMap<>(Permap); //to sort 
                        System.out.println("\nMap of People");
                        for (Map.Entry t : Sortmap.entrySet()) 
                        {
                            System.out.println(t.getKey() + " " + t.getValue());
                        }
                        
                        break;
                    case 8 : //end 
                        System.out.println("\nThank You for Using this Programme! Bye!");
                        choice = 8;
                        break;
                }                        
            } while (choice != 8);
            writer.close();
            outWrite.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println();
        }
        
        //decalre classes 
        OneBed b1 = new OneBed();
        TwoBed b2 = new TwoBed(); 
        ThreeBed b3 = new ThreeBed();     
    }
    
    public static double calcMonthlyDue(char type, char occ, int park)
    {
        double MAD = 0, areaM2;
        OneBed b1 = new OneBed(); TwoBed b2 = new TwoBed(); ThreeBed b3 = new ThreeBed();
        switch (type)
        {
            case '1' : 
                    areaM2 = (b1.area + (park*12.5));
                    if (occ == 'L')
                        MAD = areaM2*b1.lesseeR;
                    else
                        MAD = areaM2*b1.ownerR;
                    break;
            case '2' : 
                    areaM2 = (b2.area + (park*12.5));
                    if (occ == 'L')
                        MAD = areaM2*b2.lesseeR;
                    else
                        MAD = areaM2*b2.ownerR;
                    break;
            case '3' : 
                    areaM2 = (b3.area + (park*12.5));
                    if (occ == 'L')
                        MAD = areaM2*b3.lesseeR;
                    else
                        MAD = areaM2*b3.ownerR;
                    break;
        }
        return MAD;
            
    }
    
    public static void menuProg()
    {
        System.out.print("Menu\n1. Add an Account\n2. Delete an Account\n3. Display An Account"
                + "\n4. Edit an Account\n5. Search an Account\n6. Display Total Monthly Dues"
                + "\n7. Display All Accounts\n8. End");
    }
    
    public static void menuEdit()
    {
        System.out.print("Edit\n1. First Name\n2. Last Name\n3. Age"
                + "\n4. Address\n5. Occupant Type\n6. Unit Type"
                + "\n7. No. of Parking Spaces\n8. Exit");
    }
    
    public static String valAge()  
    {
        String age = "";
        boolean ulit = false;
    
        while (!ulit)
        {
            try
            {
                age = scanner.nextLine();
            
                if (age.length() != 2)
                {
                    System.out.print("Must be 2 DIGITS! ");
                    throw new Exception();
                }
                
                try
                {
                    Integer.parseInt(age);
                }
                catch (Exception e)
                {
                    System.out.print("Must be NUMERIC! ");
                    throw new Exception(); 
                }
                ulit = true;
            }
            catch (Exception e)
            {
                System.out.print("Try Again = ");
            }
           // ulit = true;
        }   
        return age;     
    }
    
    public static String valID(List<Account> accounts) 
    {
        String ID = "";
        boolean ulit = false;
    
        while (!ulit)
        {
            try
            {
                ID = scanner.nextLine();
            
                if (ID.length() != 3) 
                {
                    System.out.print("Must be 3 DIGITS! ");
                    throw new Exception();
                }
                
                Iterator it = accounts.iterator();                       
                while (it.hasNext()) 
                {
                    Account ob = (Account) it.next();
                    if ((ob.getID()).equals(ID))
                    {
                        System.out.print("ID already in List! ");
                        throw new Exception();
                    } 
                } 
            
                try
                {
                    Integer.parseInt(ID);
                }
                catch (Exception e)
                {
                    System.out.print("Must be NUMERIC! ");
                    throw new Exception(); 
                }
                ulit = true;
            }
            catch (Exception e)
            {
                System.out.print("Try Again: ");
            }
        }   
        return ID;
    }
    
    public static char valUnit()
    {
        char u = 0;
        boolean ulit = false;
        
        while (ulit == false)
        {
            try
            {
                u = scanner.nextLine().charAt(0); u = Character.toUpperCase(u); 
                if (u != '1' && u != '2' && u != '3') //3-1
                    System.out.print("Must be 1/2/3 only! Try Again = ");
                else
                    ulit = true;  
            }
            catch (Exception e)
            {
                System.out.print("Invalid Input! Try Again = ");
            }
        }
        return u;
    }
    
    public static char valOcc()
    {
        char u = 0;
        boolean ulit = false;
        
        while (ulit == false)
        {
            try
            {
                u = scanner.nextLine().charAt(0); u = Character.toUpperCase(u); 
                if (u != 'L' && u != 'O') //l or o
                    System.out.print("Must be (L/O) only! Try Again = ");
                else
                    ulit = true;  
            }
            catch (Exception e)
            {
                System.out.print("Invalid Input! Try Again = ");
            }
        }   
        return u;
    }
    
    public static int valPark(char type)//1 or 2 or 3
    {
        int u = 0;
        boolean ulit = false;
        
        while (ulit == false)
        {
            try
            {
                u = Integer.parseInt(scanner.nextLine());
                if (type == '1' && (u > 1 || u < 0))
                    System.out.print("Must be 1-0 only! Try Again = ");
                else if (type == '2' && (u > 2 || u < 0))
                    System.out.print("Must be 2-0 only! Try Again = ");
                else if (type == '3' && (u > 3 || u < 0))
                    System.out.print("Must be 3-0 only! Try Again = ");
                else
                    ulit = true;     
            }
            catch (Exception e)
            {
                System.out.print("Invalid Input! Try Again = ");
            }
        }   
        return u;
    }
    
    public static int valMenu()
    {
        int u = 0;
        boolean ulit = false;
        while (ulit == false)
        {
            try
            {
                u = Integer.parseInt(scanner.nextLine());
                if (u > 8 || u < 1) //8-1 only
                    System.out.print("Must be from Selection (1-8) only! Try Again = ");
                else
                    ulit = true;  
            }
            catch (Exception e)
            {
                System.out.print("Invalid Type must be Integer only! Try Again = ");
            }
        }   
        return u;
    }
}

class Person 
{
    private String fname;
    private String lname;
    private String age;
    private String address;
    
    Person()
    {}
    
    //parameters
    Person(String fname, String lname, String age, String address)
    {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.address = address;
    }
    
    //make setters getters
    public void setFName(String fname)
    {
        this.fname = fname;
    }
    
    public String getFName()
    {
        return fname;
    }
    
    public void setLName(String fname)
    {
        this.lname = fname;
    }
    
    public String getLName()
    {
        return lname;
    }
    
    public void setAge(String age)
    {
        this.age = age;
    }
    
    public String getAge()
    {
        return age;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public String toString()
    {
        return String.format("%s %s %s %s", getFName(), getLName(), getAge(), getAddress());
    }
}

class Account implements Serializable
{
    private String ID;
    private char occuType;
    private char unitType;
    private int numPark;
    
    Account()
    {}
    
    Account (String ID, char occuType, char unitType, int numPark)
    {
        this.ID = ID;
        this.occuType = occuType;
        this.unitType = unitType;
        this.numPark = numPark;  
    }
    public void setID(String ID)
    {
        this.ID = ID;        
    }
    
    public void setOccuType(char occuType)
    {
        this.occuType = occuType;
    }
    
    public void setUnitType(char unitType)
    {
        this.unitType = unitType;
    }
    
    public void setNumPark(int numPark)
    {
        this.numPark = numPark;
    }
    
    public String getID()
    {
        return ID;        
    }
    
    public char getOccuType()
    {
        return occuType;
    }
    
    public char getUnitType()
    {
        return unitType;
    }
    
    public int getNumPark()
    {
        return numPark;
    }
    
    public String toString()
    {
        return String.format("%S %c %c %d", getID(), getOccuType(), getUnitType(), getNumPark());
    }
          
}

class OneBed 
{
    OneBed()
    {}
    
    protected String name = "1-Bedroom"; 
    protected int bednum = 1; //no. of bedrooms 
    protected int area = 30;
    protected double lesseeR = 89.5; //rates
    protected double ownerR = 85.75;   
}

class TwoBed 
{
    TwoBed()
    {}
    
    protected String name = "2-Bedroom"; 
    protected int bednum = 2; //no. of bedrooms 
    protected int area = 65;
    protected double lesseeR = 95.75; //rates
    protected double ownerR = 89.5;   
}

class ThreeBed 
{
    ThreeBed()
    {}
    
    protected String name = "3-Bedroom"; 
    protected int bednum = 3; //no. of bedrooms also number of allowable parking
    protected int area = 96;
    protected double lesseeR = 102.8; //rates
    protected double ownerR = 93.25;    
}

class EndFile implements Serializable 
{ } 

class compareID implements Comparator<Account> 
{
    public int compare(Account acc1, Account acc2) 
    {
        return acc1.getID().compareTo(acc2.getID());
    }
}