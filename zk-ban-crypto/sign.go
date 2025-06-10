package zkbancrypto

import (
	"github.com/akakou/zk-ban-system/core"
)

func Sign(message []byte, count int64, signer []byte, gpk []byte, prover []byte) []byte {
	signature, err := core.Sign(message, count, signer, gpk, prover)

	result := Result{
		Result: signature,
		Error:  err,
	}

	return result.Output()
}
