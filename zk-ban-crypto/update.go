package zkbancrypto

import (
	client "github.com/akakou/zk-ban-system/client"
)

type UpdateRequest struct {
	Before        int64
	After         int64
	UserPublicKey []byte
	Proof         []byte
}

func RequestUpdate(signer, rl, gpk, prover []byte, url string) []byte {
	newSigner, err := client.RequestUpdate(signer, rl, gpk, prover, url)
	result := Result{
		Result: newSigner,
		Error:  err,
	}

	return result.Output()
}
