package com.hisun.lemon.gateway.common.signature;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.security.Digests;
import com.hisun.lemon.common.utils.Encodes;
import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.gateway.common.GatewayHelper;

public abstract class AbstractSignatureResolver implements SignatureResolver {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractSignatureResolver.class);

    @Override
    public boolean shouldVerify() {
        if(alreadySignatured()) {
            return false;
        }
        return doShouldVerify();
    }

    protected abstract boolean alreadySignatured();
    
    protected abstract boolean doShouldVerify();
    
    /**
     * 验签
     * @param algorithm
     * @param secureKey
     * @param verifyMsg
     * @param signedMsg
     * @return
     */
    protected boolean doVerify(Algorithm algorithm, String secureKey, String verifyMsg, String signedMsg) {
        byte[] afterSignArray = null;
        switch (algorithm) {
        case MD5:
            afterSignArray = Digests.md5((verifyMsg + secureKey).getBytes());
            break;
        case SHA1:
            afterSignArray = Digests.sha1((verifyMsg + secureKey).getBytes());
            break;
        default:
            LemonException.throwLemonException(ErrorMsgCode.SIGNATURE_EXCEPTION.getMsgCd(),
                "Unsupport algorithm \""+algorithm+"\" for signature.");
            break;
        }
        boolean flag = StringUtils.equals(signedMsg, Encodes.encodeHex(afterSignArray));
        if(! flag && logger.isErrorEnabled()) {
            logger.error("Failed to verify, request uri \"{}\", Signed str \"{}\", verify str \"{}\", secure \"{}\", algorithm \"{}\", after signed str \"{}\".", 
                Optional.ofNullable(GatewayHelper.getHttpServletRequest()).map(r -> r.getRequestURI()).orElse("UNKNOW"), signedMsg, verifyMsg, secureKey, algorithm, Encodes.encodeHex(afterSignArray));
        }
        setAlreadySignatured();
        return flag;
    }
    
    protected abstract void setAlreadySignatured();
}
