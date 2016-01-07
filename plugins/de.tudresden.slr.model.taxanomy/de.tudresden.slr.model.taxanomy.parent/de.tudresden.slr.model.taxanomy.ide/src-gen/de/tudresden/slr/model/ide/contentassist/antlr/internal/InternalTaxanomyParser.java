package de.tudresden.slr.model.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import de.tudresden.slr.model.services.TaxanomyGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTaxanomyParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_ML_COMMENT", "RULE_WS", "RULE_NEWLINE", "RULE_INT", "RULE_STRING", "RULE_SL_COMMENT", "RULE_ANY_OTHER", "'{'", "'}'", "','"
    };
    public static final int RULE_ID=4;
    public static final int RULE_WS=6;
    public static final int RULE_NEWLINE=7;
    public static final int RULE_STRING=9;
    public static final int RULE_ANY_OTHER=11;
    public static final int RULE_SL_COMMENT=10;
    public static final int RULE_INT=8;
    public static final int RULE_ML_COMMENT=5;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int EOF=-1;

    // delegates
    // delegators


        public InternalTaxanomyParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalTaxanomyParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalTaxanomyParser.tokenNames; }
    public String getGrammarFileName() { return "InternalTaxanomy.g"; }


    	private TaxanomyGrammarAccess grammarAccess;

    	public void setGrammarAccess(TaxanomyGrammarAccess grammarAccess) {
    		this.grammarAccess = grammarAccess;
    	}

    	@Override
    	protected Grammar getGrammar() {
    		return grammarAccess.getGrammar();
    	}

    	@Override
    	protected String getValueForTokenName(String tokenName) {
    		return tokenName;
    	}



    // $ANTLR start "entryRuleModel"
    // InternalTaxanomy.g:53:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // InternalTaxanomy.g:54:1: ( ruleModel EOF )
            // InternalTaxanomy.g:55:1: ruleModel EOF
            {
             before(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            ruleModel();

            state._fsp--;

             after(grammarAccess.getModelRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalTaxanomy.g:62:1: ruleModel : ( ( rule__Model__DimensionsAssignment )* ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:66:2: ( ( ( rule__Model__DimensionsAssignment )* ) )
            // InternalTaxanomy.g:67:2: ( ( rule__Model__DimensionsAssignment )* )
            {
            // InternalTaxanomy.g:67:2: ( ( rule__Model__DimensionsAssignment )* )
            // InternalTaxanomy.g:68:3: ( rule__Model__DimensionsAssignment )*
            {
             before(grammarAccess.getModelAccess().getDimensionsAssignment()); 
            // InternalTaxanomy.g:69:3: ( rule__Model__DimensionsAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalTaxanomy.g:69:4: rule__Model__DimensionsAssignment
            	    {
            	    pushFollow(FOLLOW_3);
            	    rule__Model__DimensionsAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             after(grammarAccess.getModelAccess().getDimensionsAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleTerm"
    // InternalTaxanomy.g:78:1: entryRuleTerm : ruleTerm EOF ;
    public final void entryRuleTerm() throws RecognitionException {
        try {
            // InternalTaxanomy.g:79:1: ( ruleTerm EOF )
            // InternalTaxanomy.g:80:1: ruleTerm EOF
            {
             before(grammarAccess.getTermRule()); 
            pushFollow(FOLLOW_1);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getTermRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleTerm"


    // $ANTLR start "ruleTerm"
    // InternalTaxanomy.g:87:1: ruleTerm : ( ( rule__Term__Group__0 ) ) ;
    public final void ruleTerm() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:91:2: ( ( ( rule__Term__Group__0 ) ) )
            // InternalTaxanomy.g:92:2: ( ( rule__Term__Group__0 ) )
            {
            // InternalTaxanomy.g:92:2: ( ( rule__Term__Group__0 ) )
            // InternalTaxanomy.g:93:3: ( rule__Term__Group__0 )
            {
             before(grammarAccess.getTermAccess().getGroup()); 
            // InternalTaxanomy.g:94:3: ( rule__Term__Group__0 )
            // InternalTaxanomy.g:94:4: rule__Term__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Term__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleTerm"


    // $ANTLR start "rule__Term__Group__0"
    // InternalTaxanomy.g:102:1: rule__Term__Group__0 : rule__Term__Group__0__Impl rule__Term__Group__1 ;
    public final void rule__Term__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:106:1: ( rule__Term__Group__0__Impl rule__Term__Group__1 )
            // InternalTaxanomy.g:107:2: rule__Term__Group__0__Impl rule__Term__Group__1
            {
            pushFollow(FOLLOW_4);
            rule__Term__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__0"


    // $ANTLR start "rule__Term__Group__0__Impl"
    // InternalTaxanomy.g:114:1: rule__Term__Group__0__Impl : ( ( rule__Term__NameAssignment_0 ) ) ;
    public final void rule__Term__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:118:1: ( ( ( rule__Term__NameAssignment_0 ) ) )
            // InternalTaxanomy.g:119:1: ( ( rule__Term__NameAssignment_0 ) )
            {
            // InternalTaxanomy.g:119:1: ( ( rule__Term__NameAssignment_0 ) )
            // InternalTaxanomy.g:120:2: ( rule__Term__NameAssignment_0 )
            {
             before(grammarAccess.getTermAccess().getNameAssignment_0()); 
            // InternalTaxanomy.g:121:2: ( rule__Term__NameAssignment_0 )
            // InternalTaxanomy.g:121:3: rule__Term__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__Term__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__0__Impl"


    // $ANTLR start "rule__Term__Group__1"
    // InternalTaxanomy.g:129:1: rule__Term__Group__1 : rule__Term__Group__1__Impl ;
    public final void rule__Term__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:133:1: ( rule__Term__Group__1__Impl )
            // InternalTaxanomy.g:134:2: rule__Term__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Term__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__1"


    // $ANTLR start "rule__Term__Group__1__Impl"
    // InternalTaxanomy.g:140:1: rule__Term__Group__1__Impl : ( ( rule__Term__Group_1__0 )? ) ;
    public final void rule__Term__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:144:1: ( ( ( rule__Term__Group_1__0 )? ) )
            // InternalTaxanomy.g:145:1: ( ( rule__Term__Group_1__0 )? )
            {
            // InternalTaxanomy.g:145:1: ( ( rule__Term__Group_1__0 )? )
            // InternalTaxanomy.g:146:2: ( rule__Term__Group_1__0 )?
            {
             before(grammarAccess.getTermAccess().getGroup_1()); 
            // InternalTaxanomy.g:147:2: ( rule__Term__Group_1__0 )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==12) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalTaxanomy.g:147:3: rule__Term__Group_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Term__Group_1__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getTermAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__1__Impl"


    // $ANTLR start "rule__Term__Group_1__0"
    // InternalTaxanomy.g:156:1: rule__Term__Group_1__0 : rule__Term__Group_1__0__Impl rule__Term__Group_1__1 ;
    public final void rule__Term__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:160:1: ( rule__Term__Group_1__0__Impl rule__Term__Group_1__1 )
            // InternalTaxanomy.g:161:2: rule__Term__Group_1__0__Impl rule__Term__Group_1__1
            {
            pushFollow(FOLLOW_5);
            rule__Term__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__0"


    // $ANTLR start "rule__Term__Group_1__0__Impl"
    // InternalTaxanomy.g:168:1: rule__Term__Group_1__0__Impl : ( '{' ) ;
    public final void rule__Term__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:172:1: ( ( '{' ) )
            // InternalTaxanomy.g:173:1: ( '{' )
            {
            // InternalTaxanomy.g:173:1: ( '{' )
            // InternalTaxanomy.g:174:2: '{'
            {
             before(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_1_0()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__0__Impl"


    // $ANTLR start "rule__Term__Group_1__1"
    // InternalTaxanomy.g:183:1: rule__Term__Group_1__1 : rule__Term__Group_1__1__Impl rule__Term__Group_1__2 ;
    public final void rule__Term__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:187:1: ( rule__Term__Group_1__1__Impl rule__Term__Group_1__2 )
            // InternalTaxanomy.g:188:2: rule__Term__Group_1__1__Impl rule__Term__Group_1__2
            {
            pushFollow(FOLLOW_5);
            rule__Term__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group_1__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__1"


    // $ANTLR start "rule__Term__Group_1__1__Impl"
    // InternalTaxanomy.g:195:1: rule__Term__Group_1__1__Impl : ( ( rule__Term__Group_1_1__0 )* ) ;
    public final void rule__Term__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:199:1: ( ( ( rule__Term__Group_1_1__0 )* ) )
            // InternalTaxanomy.g:200:1: ( ( rule__Term__Group_1_1__0 )* )
            {
            // InternalTaxanomy.g:200:1: ( ( rule__Term__Group_1_1__0 )* )
            // InternalTaxanomy.g:201:2: ( rule__Term__Group_1_1__0 )*
            {
             before(grammarAccess.getTermAccess().getGroup_1_1()); 
            // InternalTaxanomy.g:202:2: ( rule__Term__Group_1_1__0 )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==RULE_ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalTaxanomy.g:202:3: rule__Term__Group_1_1__0
            	    {
            	    pushFollow(FOLLOW_3);
            	    rule__Term__Group_1_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

             after(grammarAccess.getTermAccess().getGroup_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__1__Impl"


    // $ANTLR start "rule__Term__Group_1__2"
    // InternalTaxanomy.g:210:1: rule__Term__Group_1__2 : rule__Term__Group_1__2__Impl ;
    public final void rule__Term__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:214:1: ( rule__Term__Group_1__2__Impl )
            // InternalTaxanomy.g:215:2: rule__Term__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Term__Group_1__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__2"


    // $ANTLR start "rule__Term__Group_1__2__Impl"
    // InternalTaxanomy.g:221:1: rule__Term__Group_1__2__Impl : ( '}' ) ;
    public final void rule__Term__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:225:1: ( ( '}' ) )
            // InternalTaxanomy.g:226:1: ( '}' )
            {
            // InternalTaxanomy.g:226:1: ( '}' )
            // InternalTaxanomy.g:227:2: '}'
            {
             before(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_1_2()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__2__Impl"


    // $ANTLR start "rule__Term__Group_1_1__0"
    // InternalTaxanomy.g:237:1: rule__Term__Group_1_1__0 : rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1 ;
    public final void rule__Term__Group_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:241:1: ( rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1 )
            // InternalTaxanomy.g:242:2: rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1
            {
            pushFollow(FOLLOW_6);
            rule__Term__Group_1_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group_1_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1__0"


    // $ANTLR start "rule__Term__Group_1_1__0__Impl"
    // InternalTaxanomy.g:249:1: rule__Term__Group_1_1__0__Impl : ( ( rule__Term__SubclassesAssignment_1_1_0 ) ) ;
    public final void rule__Term__Group_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:253:1: ( ( ( rule__Term__SubclassesAssignment_1_1_0 ) ) )
            // InternalTaxanomy.g:254:1: ( ( rule__Term__SubclassesAssignment_1_1_0 ) )
            {
            // InternalTaxanomy.g:254:1: ( ( rule__Term__SubclassesAssignment_1_1_0 ) )
            // InternalTaxanomy.g:255:2: ( rule__Term__SubclassesAssignment_1_1_0 )
            {
             before(grammarAccess.getTermAccess().getSubclassesAssignment_1_1_0()); 
            // InternalTaxanomy.g:256:2: ( rule__Term__SubclassesAssignment_1_1_0 )
            // InternalTaxanomy.g:256:3: rule__Term__SubclassesAssignment_1_1_0
            {
            pushFollow(FOLLOW_2);
            rule__Term__SubclassesAssignment_1_1_0();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getSubclassesAssignment_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1__0__Impl"


    // $ANTLR start "rule__Term__Group_1_1__1"
    // InternalTaxanomy.g:264:1: rule__Term__Group_1_1__1 : rule__Term__Group_1_1__1__Impl ;
    public final void rule__Term__Group_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:268:1: ( rule__Term__Group_1_1__1__Impl )
            // InternalTaxanomy.g:269:2: rule__Term__Group_1_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Term__Group_1_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1__1"


    // $ANTLR start "rule__Term__Group_1_1__1__Impl"
    // InternalTaxanomy.g:275:1: rule__Term__Group_1_1__1__Impl : ( ( ',' )? ) ;
    public final void rule__Term__Group_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:279:1: ( ( ( ',' )? ) )
            // InternalTaxanomy.g:280:1: ( ( ',' )? )
            {
            // InternalTaxanomy.g:280:1: ( ( ',' )? )
            // InternalTaxanomy.g:281:2: ( ',' )?
            {
             before(grammarAccess.getTermAccess().getCommaKeyword_1_1_1()); 
            // InternalTaxanomy.g:282:2: ( ',' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==14) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalTaxanomy.g:282:3: ','
                    {
                    match(input,14,FOLLOW_2); 

                    }
                    break;

            }

             after(grammarAccess.getTermAccess().getCommaKeyword_1_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1__1__Impl"


    // $ANTLR start "rule__Model__DimensionsAssignment"
    // InternalTaxanomy.g:291:1: rule__Model__DimensionsAssignment : ( ruleTerm ) ;
    public final void rule__Model__DimensionsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:295:1: ( ( ruleTerm ) )
            // InternalTaxanomy.g:296:2: ( ruleTerm )
            {
            // InternalTaxanomy.g:296:2: ( ruleTerm )
            // InternalTaxanomy.g:297:3: ruleTerm
            {
             before(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__DimensionsAssignment"


    // $ANTLR start "rule__Term__NameAssignment_0"
    // InternalTaxanomy.g:306:1: rule__Term__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Term__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:310:1: ( ( RULE_ID ) )
            // InternalTaxanomy.g:311:2: ( RULE_ID )
            {
            // InternalTaxanomy.g:311:2: ( RULE_ID )
            // InternalTaxanomy.g:312:3: RULE_ID
            {
             before(grammarAccess.getTermAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getNameIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__NameAssignment_0"


    // $ANTLR start "rule__Term__SubclassesAssignment_1_1_0"
    // InternalTaxanomy.g:321:1: rule__Term__SubclassesAssignment_1_1_0 : ( ruleTerm ) ;
    public final void rule__Term__SubclassesAssignment_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:325:1: ( ( ruleTerm ) )
            // InternalTaxanomy.g:326:2: ( ruleTerm )
            {
            // InternalTaxanomy.g:326:2: ( ruleTerm )
            // InternalTaxanomy.g:327:3: ruleTerm
            {
             before(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_0_0()); 
            pushFollow(FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__SubclassesAssignment_1_1_0"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000002010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000004000L});

}