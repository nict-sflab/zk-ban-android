package zkbancrypto

import (
	"bytes"
	"encoding/json"
	"net/http"

	highlevel "github.com/akakou/zk-ban/highlevel"
)

type JoinRequest struct {
	Period        int64
	UserPublicKey []byte
	Proof         []byte
}

func Register(period int64, prover []byte, url string) []byte {
	signer, err := register(period, prover, url)
	result := Result{
		Result: signer,
		Error:  err,
	}

	return result.Output()
}

func register(period int64, prover []byte, url string) (*highlevel.HighLevelSigner, error) {
	proverObj := highlevel.HighLevelSnarkProver{}
	err := json.Unmarshal(prover, &proverObj)

	if err != nil {
		return nil, err
	}

	proof, reqObj, err := highlevel.JoinRequest(period, &proverObj)
	if err != nil {
		return nil, err
	}

	req := JoinRequest{
		Period:        reqObj.Period,
		UserPublicKey: reqObj.UserPublicKey,
		Proof:         proof,
	}

	reqBytes, err := json.Marshal(req)
	if err != nil {
		return nil, err
	}

	res, err := http.Post(url, "application/json", bytes.NewBuffer(reqBytes))
	if err != nil {
		return nil, err
	}

	defer res.Body.Close()

	buf := new(bytes.Buffer)
	_, err = buf.ReadFrom(res.Body)
	if err != nil {
		return nil, err
	}

	signer := highlevel.HighLevelSigner{
		Credential:    buf.Bytes(),
		Secret:        reqObj.UserSecretKey,
		UserPublicKey: reqObj.UserPublicKey,
		Period:        period,
	}

	return &signer, nil
}
