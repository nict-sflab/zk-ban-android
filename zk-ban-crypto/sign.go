package zkbancrypto

import (
	"encoding/json"

	highlevel "github.com/akakou/zk-ban/highlevel"
)

func Sign(message []byte, count int64, signer []byte, gpk []byte, prover []byte) []byte {
	proverObj := highlevel.HighLevelSnarkProver{}
	err := json.Unmarshal(prover, &proverObj)

	if err != nil {
		result := Result{
			Result: nil,
			Error:  err,
		}
		return result.Output()
	}

	signerObj := highlevel.HighLevelSigner{}
	err = json.Unmarshal(signer, &signerObj)

	if err != nil {
		result := Result{
			Result: nil,
			Error:  err,
		}
		return result.Output()
	}

	signature, err := highlevel.Sign(
		message,
		count,
		signerObj,
		gpk,
		&proverObj,
	)

	result := Result{
		Result: signature,
		Error:  err,
	}

	return result.Output()
}
