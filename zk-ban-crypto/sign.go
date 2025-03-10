package zkbancrypto

import (
	"encoding/json"

	highlevel "github.com/akakou/zk-ban/highlevel"
)

func Sign(message []byte, count int64, signer []byte, gpk []byte, prover []byte) []byte {
	signature, err := sign(message, count, signer, gpk, prover)

	result := Result{
		Result: signature,
		Error:  err,
	}

	return result.Output()
}

func sign(message []byte, count int64, signer []byte, gpk []byte, prover []byte) (*highlevel.Signature, error) {
	proverObj := highlevel.HighLevelSnarkProver{}
	err := json.Unmarshal(prover, &proverObj)

	if err != nil {
		return nil, err
	}

	signerObj := highlevel.HighLevelSigner{}
	err = json.Unmarshal(signer, &signerObj)

	if err != nil {
		return nil, err
	}

	signature, err := highlevel.Sign(
		message,
		count,
		signerObj,
		gpk,
		&proverObj,
	)

	return signature, err
}
